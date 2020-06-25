$(function () {
   $(".card-tools .btn-tool").on("click",function(){
      $(this).find("i").toggleClass("fa-caret-up fa-caret-down");
   });

   var current_url = window.location.href;
   $("#admin-menu a.nav-link").each(function(){
      var item = $(this);
      var url = item.attr("href");
      if(url != null && url != "#" && url != ""){
         if(current_url.indexOf(url) > 0){
            $("a.nav-link").removeClass("active");
            item.addClass("active");
            item.parents("ul").parents("li").addClass("menu-open");
            item.parents("ul").parents("li").find("a.nav-link").addClass("active");
         }
      }
   });
   $("a.disabled").each(function(){
      $(this).attr("href","javascript:void(0);");
   });
});