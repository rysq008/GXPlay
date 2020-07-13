package com.zhny.zhny_app.utils;


import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public class UploadUtils {

    public abstract static class FileUploadProgress {

        //监听进度的改变
        public void onProgressChange(final long bytesWritten, final long contentLength) {
            MainThreadPostUtils.post(new Runnable() {
                @Override
                public void run() {
                    onProgress((int) (bytesWritten * 100 / contentLength));
                }
            });
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
}
