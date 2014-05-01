/**
 * Created by dungvn3000 on 2/1/14.
 */

$(function() {
    var $scrolltotop = $("#scrolltotop");
    $scrolltotop.css('display', 'none');

    $(function () {
        $(window).scroll(function () {
            if ($(this).scrollTop() > 100) {
                $scrolltotop.slideDown('fast');
            } else {
                $scrolltotop.slideUp('fast');
            }
        });

        $scrolltotop.click(function () {
            $('body,html').animate({
                scrollTop: 0
            }, 'fast');
            return false;
        });
    });

    $('.bxslider').bxSlider({
        auto: true,
        pager: false
    });
});