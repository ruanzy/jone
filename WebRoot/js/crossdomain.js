var url = 'http://localhost:8088/JOne/common/cookies';
var scriptBlock = document.createElement("script");
scriptBlock.setAttribute('src', url);
scriptBlock.type = "text/javascript";
document.getElementsByTagName("head")[0].appendChild(scriptBlock);