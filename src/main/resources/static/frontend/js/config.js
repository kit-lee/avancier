var isTest = false; //ΪtrueʱΪ���Ի���������ʽ����ʱ��Ҫ�ʸ�config.app_id��config_id2����

var  config ={};
/***���õ�*******************/
config.erWeima        =  "http://czldly.91zmt.com/map/images/qrcode.jpg"; //�����ȥ��ע�ö�ά��

/**********************/
//�̵�  (���������ļ����Ի�����������ͨ��)
config.app_id         =  "wx6306fd06b0d683cc";//"wx6306fd06b0d683cc";//�̵����������
config.webcode        =  "czldly"; //luofushan czldly
config.aadress        =  "�����̵�"; //ÿ��������λ�ĵ�ͼĬ�ϵ�ַ
config.app_id2        =  isTest ? "wx8208082617097c70" : "wx8208082617097c70";  //ͳһ�� ������ ��ȡ�û���Ȩ
config.shouquan       =  "http://"+(isTest?"test.":"")+"evote.91zmt.com/avanicer/first2.html"; //��ȥ���û���Ϣ��ҳ��
config.mapUrl         =  "http://"+(isTest?"test.":"")+"evote.91zmt.com/avanicer/index.html"; //��ͼlink
config.liuYanUrl      =  "http://"+(isTest?"test.":"")+"evote.91zmt.com/avanicer/liuyan.html";//���԰�
config.myLiuYanUrl    =  "http://"+(isTest?"test.":"")+"evote.91zmt.com/avanicer/myliuyan.html";//������������link
config.shareImage     =  "http://"+(isTest?"test.":"")+"czldly.91zmt.com/map/images/czldly.jpg";//����ͼ��
config.liuyanServer   =  isTest ? "http://test.work.91zmt.com/muses-wxliuyan/" : "http://weixin.91zmt.com/liuyan/";



