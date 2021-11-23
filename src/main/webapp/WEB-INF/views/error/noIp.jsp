<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
<style>
  
  .layer_error_box	{position:absolute; width:100%; top:20%;z-index:1000}

  
  .error_Wrap 	 { position:relative; margin:15% auto;}
  .error_Wrap .box { width:716px; height:170px; margin:0 auto; background:url("https://www.crefia.or.kr/static/images/portal/common/img_error_bg.png") no-repeat;}
  .error_Wrap .box img { margin:24px 0 0 75px; float:left}
  .error_Wrap .box .text { width:485px; margin:20px 30px 0 10px; float:right;}
  .error_Wrap .box .text h1 { font-size:32px; color:#000; line-height:32px; padding:18px 0; margin-left:-5px;}
  .error_Wrap .box .text span { font-size:16px; color:#717171; line-height:23px; padding:20px 0 0 0;}

  
  .error_Wrap_mobile 	 { position:relative; width:100%; margin:30% auto;}
  .error_Wrap_mobile .box { width:55%; text-align:center; background:#0c93fe; border-radius:40px; padding:5% 5% 10% 5%; margin:0 auto;}
  .error_Wrap_mobile .box img { width:35%; margin:5% 0 10% 0;}
  .error_Wrap_mobile .box .text { margin:0 2%}
  .error_Wrap_mobile .box .text h1 { font-size:2.25em; color:#FFF; line-height:200%; padding:20px 0 10px 0; margin-left:-5px;}
  .error_Wrap_mobile .box .text span { font-size:2em; color:#FFF; line-height:140%; padding:20px 0 0 0;}
  .back {
    font-size: 14px;
    color: #fff;
    line-height: 32px;
    font-weight: bold;
    padding: 5px;
    background: #494141;
    width: 100px;
    cursor:pointer;
	}
</style>


  	<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
	<meta http-equiv="Content-Script-Type" content="text/javascript"/>
	<meta http-equiv="Content-Style-Type" content="text/css"/>
	<title>여신금융협회</title>
</head>
<body>


<div class="error_Wrap">
    <div class="box">
        <img src="https://www.crefia.or.kr/static/images/portal/common/img_error.png" alt=""/>
        <div class="text">
            <h1>접근 할 수 있는 권한이 없습니다.</h1>
            <span>문의 사항이 있을 경우 시스템 담당자에게 문의하여 주시기 <br/>바랍니다.</span>
        </div>
    </div>
</div>

</body>
</html>




