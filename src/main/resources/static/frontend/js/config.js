var isTest = false; //为true时为测试环境，改正式环境时需要问改config.app_id和config_id2参数

var  config ={};
/***公用的*******************/
config.erWeima        =  "http://czldly.91zmt.com/map/images/qrcode.jpg"; //分享出去关注用二维码

/**********************/
//绿岛  (下列配置文件测试环境参数测试通过)
config.app_id         =  "wx6306fd06b0d683cc";//"wx6306fd06b0d683cc";//绿岛用这个可以
config.webcode        =  "czldly"; //luofushan czldly
config.aadress        =  "潮州绿岛"; //每个景区定位的地图默认地址
config.app_id2        =  isTest ? "wx8208082617097c70" : "wx8208082617097c70";  //统一用 哇体验 获取用户授权
config.shouquan       =  "http://"+(isTest?"test.":"")+"evote.91zmt.com/avanicer/first2.html"; //跳去拿用户信息的页面
config.mapUrl         =  "http://"+(isTest?"test.":"")+"evote.91zmt.com/avanicer/index.html"; //地图link
config.liuYanUrl      =  "http://"+(isTest?"test.":"")+"evote.91zmt.com/avanicer/liuyan.html";//留言版
config.myLiuYanUrl    =  "http://"+(isTest?"test.":"")+"evote.91zmt.com/avanicer/myliuyan.html";//个人留言中心link
config.shareImage     =  "http://"+(isTest?"test.":"")+"czldly.91zmt.com/map/images/czldly.jpg";//分享图标
config.liuyanServer   =  isTest ? "http://test.work.91zmt.com/muses-wxliuyan/" : "http://weixin.91zmt.com/liuyan/";



