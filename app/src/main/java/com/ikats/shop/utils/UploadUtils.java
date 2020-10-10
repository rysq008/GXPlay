package com.ikats.shop.utils;


import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ForwardingSink;
import okio.ForwardingSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

public class UploadUtils {

    public abstract static class FileUploadProgress {
        //监听进度的改变
        public void onProgressChange(final long bytesWritten, final long contentLength) {
            MainThreadPostUtils.post(() -> onProgress((int) (bytesWritten * 100 / contentLength)));
        }

        //上传进度回调
        public abstract void onProgress(int progress);
    }

    public abstract static class FileDownloadProgress {
        //监听进度的改变
        public void onProgressChange(final long bytesWritten, final long contentLength) {
            MainThreadPostUtils.post(() -> onProgress((int) (bytesWritten * 100 / contentLength)));
        }

        //上传进度回调
        public abstract void onProgress(int progress);
    }

    public static class UploadFileRequestBody extends RequestBody {

        private RequestBody mRequestBody;
        private FileUploadProgress fileUploadProgress;


        public UploadFileRequestBody(File file, FileUploadProgress fileUploadFlowable) {
            this.mRequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
            this.fileUploadProgress = fileUploadFlowable;
        }

        @Override
        public MediaType contentType() {
            return mRequestBody.contentType();
        }

        @Override
        public long contentLength() throws IOException {
            return mRequestBody.contentLength();
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            CountingSink countingSink = new CountingSink(sink);
            BufferedSink bufferedSink = Okio.buffer(countingSink);
            //写入
            mRequestBody.writeTo(bufferedSink);

            //必须调用flush，否则最后一部分数据可能不会被写入
            bufferedSink.flush();
        }

        protected final class CountingSink extends ForwardingSink {

            private long bytesWritten = 0;

            public CountingSink(Sink delegate) {
                super(delegate);
            }

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);

                bytesWritten += byteCount;
                if (fileUploadProgress != null) {
                    fileUploadProgress.onProgressChange(bytesWritten, contentLength());
                }
            }
        }
    }

    public static class DownloadFileRequestBody extends ResponseBody {
        private ResponseBody delegate;
        private BufferedSource bufferedSource;
        private FileDownloadProgress fileDownloadProgress;

        public DownloadFileRequestBody(ResponseBody delegate, FileDownloadProgress listeners) {
            this.delegate = delegate;
            this.fileDownloadProgress = listeners;
        }

        @Override
        public MediaType contentType() {
            return delegate.contentType();
        }

        @Override
        public long contentLength() {
            return delegate.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(new ProgressSource(delegate.source()));
            }
            return bufferedSource;
        }


        final class ProgressSource extends ForwardingSource {
            private long soFarBytes = 0;
            private long totalBytes = -1;

            public ProgressSource(Source delegate) {
                super(delegate);
            }

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = 0L;
                try {
                    bytesRead = super.read(sink, byteCount);

                    if (totalBytes < 0) {
                        totalBytes = contentLength();
                    }
                    soFarBytes += (bytesRead != -1 ? bytesRead : 0);
                    if (fileDownloadProgress != null)
                        fileDownloadProgress.onProgressChange(soFarBytes, totalBytes);
                } catch (IOException e) {
                    throw e;
                }

                return bytesRead;
            }
        }
    }

}
