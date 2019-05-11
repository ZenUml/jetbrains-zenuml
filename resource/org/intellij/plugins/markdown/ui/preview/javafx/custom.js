(function() {
  // your page initialization code here
  setTimeout(function () {
    // the DOM will be available here
    var app = document.getElementById("app2"); app.innerHTML='replaced with external custom JS';
  }, 5000)
})();