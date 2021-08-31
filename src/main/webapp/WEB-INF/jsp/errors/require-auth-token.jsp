<%@include file="../parts/head.jsp" %>

<div class="orbeon">
    <h1>Waiting for Auth token to be passed and verified.</h1>
    <h3>Refresh the page if this does not happen automatically in 20 seconds</h3>
    <a class="text-blue-500" href="<%= contextPath %>/">Go Home</a>
</div>

<script>
    setInterval(function () {
        if (getCookie(authCookieName) !== null) {
            location.href = "<%= contextPath %>/"
        }
    }, 5000);
</script>

<%@include file="../parts/foot.jsp" %>