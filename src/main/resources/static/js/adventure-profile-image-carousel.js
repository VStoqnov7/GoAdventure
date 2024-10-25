(function ($) {
  const setFullHeight = () => {
    $(".js-fullheight").css("height", $(window).height());
  };

  $(window).on("resize", setFullHeight);
  setFullHeight();

  const owl = $(".featured-carousel");

  owl.owlCarousel({
    animateOut: "fadeOut",
    items: 1,
    loop: true,
    smartSpeed: 100,
    autoplay: false,
    dots: false,
    nav: false,
    navText: [
      '<span class="icon-keyboard_arrow_left">',
      '<span class="icon-keyboard_arrow_right">',
    ],
  });

  $(".thumbnail li").on("click", function (e) {
    const slideIndex = $(this).index();
    owl.trigger("to.owl.carousel", [slideIndex, 1500]);
    e.preventDefault();
  });

  owl.on("changed.owl.carousel", function (event) {
    const carouselIndex = (event.item.index - event.relatedTarget._clones.length / 2) % event.item.count;
    const actualIndex = (carouselIndex + event.item.count) % event.item.count;

    $(".thumbnail li")
      .removeClass("active")
      .eq(actualIndex)
      .addClass("active");
  });
})(jQuery);