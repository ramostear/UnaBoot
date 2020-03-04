$(function(){
   var current_url = window.location.href;
   $("#una-boot-menu a.nav-link").each(function(){
        var item = $(this);
        var url = item.attr("href");
        if(url != "#" && url != ""){
            if(current_url.indexOf(url)>0){
                $("a.nav-link").removeClass("active");
                item.addClass("active");
                item.parents("ul").parents("li").addClass("menu-open");
                item.parents("ul").parents("li").find("a.nav-link").addClass("active");
            }
        }
   });
});