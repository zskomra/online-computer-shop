 jQuery(
            function($) {
                $.scrollTo(0);
                $('#link1').click(function (){$.scrollTo($('#desc-link'),500);});
                $('#link2').click(function (){$.scrollTo($('#rati-link'),500);});
                $('.scrollup').click(function (){$.scrollTo($('body'),1000);});
            }
        );

        $(window).scroll(
            function (){
                if($(this).scrollTop()>100) $('.scrollup').fadeIn();
                else $('.scrollup').fadeOut();
            }
        );