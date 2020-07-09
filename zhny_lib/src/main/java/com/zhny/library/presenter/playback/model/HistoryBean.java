package com.zhny.library.presenter.playback.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author ClamLaw
 * @time 2019/8/28  17:06
 * @desc ${TODD}
 */
public class HistoryBean {

    /**
     * status : 1
     * msg :
     * result : [{"pt":"2019-08-29 09:46:51","lat":40.099906,"lng":113.326701,"s":"16.67","c":"349","stop":"0","stm":"0","id":"8129281","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 09:46:51<br/>速度：16.67KM/h<br/>方向：南偏西11 °<br/>停车时长：0分"},{"pt":"2019-08-29 09:47:11","lat":40.100497,"lng":113.326771,"s":"20.37","c":"1","stop":"0","stm":"0","id":"8152632","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 09:47:11<br/>速度：20.37KM/h<br/>方向：北偏东1 °<br/>停车时长：0分"},{"pt":"2019-08-29 09:47:11","lat":40.100497,"lng":113.326771,"s":"0.00","c":"1","stop":"1","stm":"0","id":"8160904","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 09:47:11<br/>速度：0.00KM/h<br/>方向：北偏东1 °<br/>停车时长：0分"},{"pt":"2019-08-29 09:47:17","lat":40.100779,"lng":113.32698,"s":"22.22","c":"24","stop":"0","stm":"0","id":"8162354","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 09:47:17<br/>速度：22.22KM/h<br/>方向：北偏东24 °<br/>停车时长：0分"},{"pt":"2019-08-29 09:47:31","lat":40.101066,"lng":113.327577,"s":"25.93","c":"53","stop":"0","stm":"0","id":"8184372","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 09:47:31<br/>速度：25.93KM/h<br/>方向：东偏北37 °<br/>停车时长：0分"},{"pt":"2019-08-29 09:47:51","lat":40.100738,"lng":113.327757,"s":"20.37","c":"57","stop":"0","stm":"0","id":"8209189","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 09:47:51<br/>速度：20.37KM/h<br/>方向：东偏北33 °<br/>停车时长：0分"},{"pt":"2019-08-29 09:48:31","lat":40.100646,"lng":113.325794,"s":"12.96","c":"341","stop":"0","stm":"0","id":"8272403","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 09:48:31<br/>速度：12.96KM/h<br/>方向：南偏西19 °<br/>停车时长：0分"},{"pt":"2019-08-29 09:48:46","lat":40.100776,"lng":113.323495,"s":"20.37","c":"293","stop":"0","stm":"0","id":"8286148","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 09:48:46<br/>速度：20.37KM/h<br/>方向：西偏北23 °<br/>停车时长：0分"},{"pt":"2019-08-29 09:48:51","lat":40.100541,"lng":113.323127,"s":"20.37","c":"269","stop":"0","stm":"0","id":"8295989","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 09:48:51<br/>速度：20.37KM/h<br/>方向：西偏南1 °<br/>停车时长：0分"},{"pt":"2019-08-29 09:48:52","lat":40.10048,"lng":113.323067,"s":"20.37","c":"253","stop":"0","stm":"0","id":"8297637","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 09:48:52<br/>速度：20.37KM/h<br/>方向：西偏南17 °<br/>停车时长：0分"},{"pt":"2019-08-29 09:49:00","lat":40.100001,"lng":113.323077,"s":"31.48","c":"210","stop":"0","stm":"0","id":"8307725","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 09:49:00<br/>速度：31.48KM/h<br/>方向：南偏西30 °<br/>停车时长：0分"},{"pt":"2019-08-29 09:50:30","lat":40.096884,"lng":113.319856,"s":"14.82","c":"258","stop":"0","stm":"0","id":"8431045","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 09:50:30<br/>速度：14.82KM/h<br/>方向：西偏南12 °<br/>停车时长：0分"},{"pt":"2019-08-29 09:50:50","lat":40.096744,"lng":113.317966,"s":"31.48","c":"268","stop":"0","stm":"0","id":"8456040","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 09:50:50<br/>速度：31.48KM/h<br/>方向：西偏南2 °<br/>停车时长：0分"},{"pt":"2019-08-29 09:51:10","lat":40.096744,"lng":113.317966,"s":"0.00","c":"265","stop":"1","stm":"0","id":"8486346","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 09:51:10<br/>速度：0.00KM/h<br/>方向：西偏南5 °<br/>停车时长：0分"},{"pt":"2019-08-29 09:59:06","lat":40.096761,"lng":113.317201,"s":"0.00","c":"193","stop":"1","stm":"0","id":"9141570","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 09:59:06<br/>速度：0.00KM/h<br/>方向：南偏西13 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:01:25","lat":40.096648,"lng":113.316445,"s":"0.00","c":"193","stop":"1","stm":"0","id":"9338494","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:01:25<br/>速度：0.00KM/h<br/>方向：南偏西13 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:02:52","lat":40.096464,"lng":113.315591,"s":"0.00","c":"237","stop":"1","stm":"0","id":"9458520","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:02:52<br/>速度：0.00KM/h<br/>方向：西偏南33 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:02:56","lat":40.096094,"lng":113.315591,"s":"22.22","c":"175","stop":"0","stm":"0","id":"9461686","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:02:56<br/>速度：22.22KM/h<br/>方向：南偏东5 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:03:04","lat":40.095464,"lng":113.315562,"s":"27.78","c":"175","stop":"0","stm":"0","id":"9471686","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:03:04<br/>速度：27.78KM/h<br/>方向：南偏东5 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:03:24","lat":40.092973,"lng":113.315484,"s":"53.71","c":"177","stop":"0","stm":"0","id":"9503224","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:03:24<br/>速度：53.71KM/h<br/>方向：南偏东3 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:03:44","lat":40.091783,"lng":113.315495,"s":"0.00","c":"181","stop":"0","stm":"0","id":"9530600","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:03:44<br/>速度：0.00KM/h<br/>方向：南偏西1 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:04:04","lat":40.091783,"lng":113.315495,"s":"0.00","c":"181","stop":"1","stm":"0","id":"9553825","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:04:04<br/>速度：0.00KM/h<br/>方向：南偏西1 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:04:43","lat":40.090656,"lng":113.315605,"s":"1.96","c":"177","stop":"0","stm":"0","id":"9608433","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:04:43<br/>速度：1.96KM/h<br/>方向：南偏东3 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:04:44","lat":40.090656,"lng":113.315605,"s":"0.00","c":"177","stop":"1","stm":"0","id":"9616701","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:04:44<br/>速度：0.00KM/h<br/>方向：南偏东3 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:05:21","lat":40.091985,"lng":113.315594,"s":"7.41","c":"60","stop":"0","stm":"0","id":"9662731","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:05:21<br/>速度：7.41KM/h<br/>方向：东偏北30 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:05:23","lat":40.092346,"lng":113.315643,"s":"35.19","c":"338","stop":"0","stm":"0","id":"9664280","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:05:23<br/>速度：35.19KM/h<br/>方向：南偏西22 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:05:38","lat":40.094855,"lng":113.315642,"s":"61.12","c":"1","stop":"0","stm":"0","id":"9685769","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:05:38<br/>速度：61.12KM/h<br/>方向：北偏东1 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:05:43","lat":40.095635,"lng":113.315651,"s":"59.26","c":"1","stop":"0","stm":"0","id":"9695617","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:05:43<br/>速度：59.26KM/h<br/>方向：北偏东1 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:05:53","lat":40.096634,"lng":113.315611,"s":"31.48","c":"359","stop":"0","stm":"0","id":"9708358","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:05:53<br/>速度：31.48KM/h<br/>方向：南偏西1 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:06:03","lat":40.096634,"lng":113.315611,"s":"0.00","c":"358","stop":"1","stm":"0","id":"9718774","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:06:03<br/>速度：0.00KM/h<br/>方向：南偏西2 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:06:23","lat":40.096425,"lng":113.31568,"s":"12.96","c":"359","stop":"0","stm":"0","id":"9749871","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:06:23<br/>速度：12.96KM/h<br/>方向：南偏西1 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:06:43","lat":40.098422,"lng":113.31553,"s":"31.48","c":"358","stop":"0","stm":"0","id":"9774491","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:06:43<br/>速度：31.48KM/h<br/>方向：南偏西2 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:07:03","lat":40.101554,"lng":113.315628,"s":"48.15","c":"0","stop":"0","stm":"0","id":"9797434","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:07:03<br/>速度：48.15KM/h<br/>方向：正北<br/>停车时长：0分"},{"pt":"2019-08-29 10:07:23","lat":40.103275,"lng":113.315726,"s":"11.11","c":"359","stop":"0","stm":"0","id":"9828815","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:07:23<br/>速度：11.11KM/h<br/>方向：南偏西1 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:07:42","lat":40.103275,"lng":113.315726,"s":"0.00","c":"353","stop":"1","stm":"0","id":"9853932","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:07:42<br/>速度：0.00KM/h<br/>方向：南偏西7 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:08:08","lat":40.103338,"lng":113.315289,"s":"14.82","c":"321","stop":"0","stm":"0","id":"9886971","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:08:08<br/>速度：14.82KM/h<br/>方向：南偏西39 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:08:22","lat":40.103227,"lng":113.312973,"s":"22.22","c":"268","stop":"0","stm":"0","id":"9908295","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:08:22<br/>速度：22.22KM/h<br/>方向：西偏南2 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:08:23","lat":40.103203,"lng":113.312715,"s":"24.08","c":"264","stop":"0","stm":"0","id":"9916676","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:08:23<br/>速度：24.08KM/h<br/>方向：西偏南6 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:08:42","lat":40.103103,"lng":113.309386,"s":"31.48","c":"269","stop":"0","stm":"0","id":"9939913","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:08:42<br/>速度：31.48KM/h<br/>方向：西偏南1 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:09:02","lat":40.103103,"lng":113.309386,"s":"0.00","c":"266","stop":"1","stm":"0","id":"9962721","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:09:02<br/>速度：0.00KM/h<br/>方向：西偏南4 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:10:01","lat":40.103342,"lng":113.30878,"s":"12.96","c":"281","stop":"0","stm":"0","id":"10041638","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:10:01<br/>速度：12.96KM/h<br/>方向：西偏北11 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:10:21","lat":40.103846,"lng":113.30387,"s":"46.30","c":"287","stop":"0","stm":"0","id":"10095416","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:10:21<br/>速度：46.30KM/h<br/>方向：西偏北17 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:10:41","lat":40.103871,"lng":113.302995,"s":"0.00","c":"281","stop":"1","stm":"0","id":"10128570","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:10:41<br/>速度：0.00KM/h<br/>方向：西偏北11 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:11:41","lat":40.103782,"lng":113.301186,"s":"44.45","c":"269","stop":"0","stm":"0","id":"10241337","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:11:41<br/>速度：44.45KM/h<br/>方向：西偏南1 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:12:01","lat":40.10343,"lng":113.297804,"s":"33.34","c":"262","stop":"0","stm":"0","id":"10241338","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:12:01<br/>速度：33.34KM/h<br/>方向：西偏南8 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:12:20","lat":40.103499,"lng":113.297744,"s":"1.85","c":"266","stop":"0","stm":"0","id":"10241336","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:12:20<br/>速度：1.85KM/h<br/>方向：西偏南4 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:12:40","lat":40.103502,"lng":113.294998,"s":"61.12","c":"270","stop":"0","stm":"0","id":"10263574","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:12:40<br/>速度：61.12KM/h<br/>方向：正西<br/>停车时长：0分"},{"pt":"2019-08-29 10:13:00","lat":40.103597,"lng":113.290924,"s":"53.71","c":"271","stop":"0","stm":"0","id":"10287293","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:13:00<br/>速度：53.71KM/h<br/>方向：西偏北1 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:13:20","lat":40.103465,"lng":113.288351,"s":"31.48","c":"258","stop":"0","stm":"0","id":"10317941","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:13:20<br/>速度：31.48KM/h<br/>方向：西偏南12 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:13:40","lat":40.103253,"lng":113.286616,"s":"16.67","c":"254","stop":"0","stm":"0","id":"10342341","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:13:40<br/>速度：16.67KM/h<br/>方向：西偏南16 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:14:00","lat":40.103024,"lng":113.284839,"s":"22.22","c":"265","stop":"0","stm":"0","id":"10365665","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:14:00<br/>速度：22.22KM/h<br/>方向：西偏南5 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:14:20","lat":40.102968,"lng":113.283231,"s":"14.82","c":"266","stop":"0","stm":"0","id":"10396944","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:14:20<br/>速度：14.82KM/h<br/>方向：西偏南4 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:14:31","lat":40.102968,"lng":113.283231,"s":"0.00","c":"296","stop":"1","stm":"0","id":"10418214","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:14:31<br/>速度：0.00KM/h<br/>方向：西偏北26 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:15:19","lat":40.103678,"lng":113.283191,"s":"11.11","c":"355","stop":"0","stm":"0","id":"10488713","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:15:19<br/>速度：11.11KM/h<br/>方向：南偏西5 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:15:35","lat":40.103678,"lng":113.283191,"s":"0.00","c":"2","stop":"1","stm":"0","id":"10518802","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:15:35<br/>速度：0.00KM/h<br/>方向：北偏东2 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:17:32","lat":40.104378,"lng":113.283261,"s":"0.00","c":"356","stop":"1","stm":"0","id":"10668489","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:17:32<br/>速度：0.00KM/h<br/>方向：南偏西4 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:17:38","lat":40.104678,"lng":113.283111,"s":"18.52","c":"348","stop":"0","stm":"0","id":"10671620","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:17:38<br/>速度：18.52KM/h<br/>方向：南偏西12 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:17:58","lat":40.105046,"lng":113.282461,"s":"0.00","c":"343","stop":"1","stm":"0","id":"10694944","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:17:58<br/>速度：0.00KM/h<br/>方向：南偏西17 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:18:04","lat":40.105176,"lng":113.282171,"s":"14.82","c":"321","stop":"0","stm":"0","id":"10705058","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:18:04<br/>速度：14.82KM/h<br/>方向：南偏西39 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:18:09","lat":40.105295,"lng":113.281881,"s":"18.52","c":"285","stop":"0","stm":"0","id":"10715028","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:18:09<br/>速度：18.52KM/h<br/>方向：西偏北15 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:20:16","lat":40.106511,"lng":113.276189,"s":"9.26","c":"355","stop":"0","stm":"0","id":"10891636","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:20:16<br/>速度：9.26KM/h<br/>方向：南偏西5 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:20:17","lat":40.106511,"lng":113.276189,"s":"0.00","c":"354","stop":"1","stm":"0","id":"10893239","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:20:17<br/>速度：0.00KM/h<br/>方向：南偏西6 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:20:37","lat":40.107354,"lng":113.275408,"s":"14.82","c":"352","stop":"0","stm":"0","id":"10916051","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:20:37<br/>速度：14.82KM/h<br/>方向：南偏西8 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:20:56","lat":40.10753,"lng":113.274186,"s":"16.67","c":"317","stop":"0","stm":"0","id":"10939326","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:20:56<br/>速度：16.67KM/h<br/>方向：南偏西43 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:20:57","lat":40.10754,"lng":113.274126,"s":"16.67","c":"304","stop":"0","stm":"0","id":"10947837","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:20:57<br/>速度：16.67KM/h<br/>方向：西偏北34 °<br/>停车时长：0分"},{"pt":"2019-08-29 10:21:15","lat":40.10754,"lng":113.274126,"s":"0.00","c":"316","stop":"1","stm":"0","id":"10974213","g":"1","time_str":"0分","desc":"9180288847&nbsp;GPS<br/>2019-08-29 10:21:15<br/>速度：0.00KM/h<br/>方向：南偏西44 °<br/>停车时长：0分"}]
     */

    private String status;
    private String msg;
    private List<ResultBean> result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean implements Serializable {
        /**
         * pt : 2019-08-29 09:46:51
         * lat : 40.099906
         * lng : 113.326701
         * s : 16.67
         * c : 349
         * stop : 0
         * stm : 0
         * id : 8129281
         * g : 1
         * time_str : 0分
         * desc : 9180288847&nbsp;GPS<br/>2019-08-29 09:46:51<br/>速度：16.67KM/h<br/>方向：南偏西11 °<br/>停车时长：0分
         */

        private String pt;
        private double lat;
        private double lng;
        private String s;
        private String c;
        private String stop;
        private String stm;
        private String id;
        private String g;
        private String time_str;
        private String desc;

        public String getPt() {
            return pt;
        }

        public void setPt(String pt) {
            this.pt = pt;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }

        public String getStop() {
            return stop;
        }

        public void setStop(String stop) {
            this.stop = stop;
        }

        public String getStm() {
            return stm;
        }

        public void setStm(String stm) {
            this.stm = stm;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getG() {
            return g;
        }

        public void setG(String g) {
            this.g = g;
        }

        public String getTime_str() {
            return time_str;
        }

        public void setTime_str(String time_str) {
            this.time_str = time_str;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
