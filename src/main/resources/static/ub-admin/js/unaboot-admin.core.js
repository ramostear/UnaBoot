$(function () {
   $(".card-tools .btn-tool").on("click",function(){
      $(this).find("i").toggleClass("fa-caret-up fa-caret-down");
   });
});