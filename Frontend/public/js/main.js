(function ($) {
    "use strict";

    // Spinner
    var spinner = function () {
        setTimeout(function () {
            if ($('#spinner').length > 0) {
                $('#spinner').removeClass('show');
            }
        }, 1);
    };
    spinner();
    
    
    // Initiate the wowjs
    new WOW().init();


    // Sticky Navbar
    $(window).scroll(function () {
        if ($(this).scrollTop() > 45) {
            $('.nav-bar').addClass('sticky-top');
        } else {
            $('.nav-bar').removeClass('sticky-top');
        }
    });
    
    
    // Back to top button
    $(window).scroll(function () {
        if ($(this).scrollTop() > 300) {
            $('.back-to-top').fadeIn('slow');
        } else {
            $('.back-to-top').fadeOut('slow');
        }
    });
    $('.back-to-top').click(function () {
        $('html, body').animate({scrollTop: 0}, 1500, 'easeInOutExpo');
        return false;
    });


    // Header carousel
    $(".header-carousel").owlCarousel({
        autoplay: true,
        smartSpeed: 1500,
        items: 1,
        dots: true,
        loop: true,
        nav : true,
        navText : [
            '<i class="bi bi-chevron-left"></i>',
            '<i class="bi bi-chevron-right"></i>'
        ]
    });


    // Testimonials carousel
    $(".testimonial-carousel").owlCarousel({
        autoplay: true,
        smartSpeed: 1000,
        margin: 24,
        dots: false,
        loop: true,
        nav : true,
        navText : [
            '<i class="bi bi-arrow-left"></i>',
            '<i class="bi bi-arrow-right"></i>'
        ],
        responsive: {
            0:{
                items:1
            },
            992:{
                items:2
            }
        }
    });

    // datepicker.js

    $(function(){
        $('#datepicker').datepicker();
      });
      let startDate = document.getElementById('startDate')
        let endDate = document.getElementById('endDate')

        startDate.addEventListener('change',(e)=>{
        let startDateVal = e.target.value
        document.getElementById('startDateSelected').innerText = startDateVal
        })

        endDate.addEventListener('change',(e)=>{
        let endDateVal = e.target.value
        document.getElementById('endDateSelected').innerText = endDateVal
        })  


        // // Function to show more team members
        //     function showMoreMembers() {
        //         // Get the extra team members
        //         var extraMembers = document.querySelectorAll('.extra-member');
            
        //         // Toggle the visibility of extra members
        //         extraMembers.forEach(function(member) {
        //         member.classList.toggle('d-none');
        //         });
            
        //         // Change button text based on visibility
        //         var buttonText = document.getElementById('showMoreButton').querySelector('button');
        //         if (buttonText.innerText === 'Show More') {
        //         buttonText.innerText = 'Show Less';
        //         } else {
        //         buttonText.innerText = 'Show More';
        //         }
        //     }
  
    
})(jQuery);

