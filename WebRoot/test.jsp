<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>

    <style type="text/css">
        body {
           font-size:13px;
           line-height:22px;
           color:#333333; 
        }
    </style>
</head>
<body>
<div style="float: left;">
<button id='addbtn' class='btn btn-success' funcid='102'><i class="icon-plus-sign"></i> 增加</button>
<button id='delbtn' class='btn btn-success' funcid='103'><i class="icon-pencil"></i> 修改</button>
<button id='assignbtn' class='btn btn-success' funcid='104'><i class="icon-remove-sign"></i> 删除</button>
</div>


    <h2>wangHighLighter demo</h2>
    <div id="optionsDiv">
        语言：
        <select id="sltLang">
        </select>
        &nbsp;&nbsp;
        主题：
        <select id="sltTheme">
        </select>
    </div>
    <textarea id="txt1" rows="10" cols="10" style="width:100%;"></textarea>
    <button id="btn1" type="button">转换</button>
    <hr />
    <div id="div1"></div>

    <script type="text/javascript">
       
            var $sltLang = $('#sltLang'),
                langArray = HighLighter.getLangArray(),
                $sltTheme = $('#sltTheme'),
                themeArray = HighLighter.getThemeArray(),
                item,
                $txt1 = $('#txt1'),
                $div1 = $('#div1'),
                $btn1 = $('#btn1');
            
            for (item in langArray) {
                $sltLang.append($('<option>' + langArray[item] + '</option>'));
            }
            for (item in themeArray) {
                $sltTheme.append($('<option>' + themeArray[item] + '</option>'));
            }
            $btn1.click(function () {
                var code = $txt1.val(),
                    lang = $sltLang.val(),
                    theme = $sltTheme.val(),
                    highLightCode;
                highLightCode = HighLighter.highLight(lang, theme, code);
                $div1.html(highLightCode);
            });
            
  
    </script>
</body>
</html>