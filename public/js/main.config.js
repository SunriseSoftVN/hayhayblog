/***************************************************************************************************
====================================================================================================


    Sunrise - Coming Soon Page

    v1.0, 21 December 2012

    by Alex Shnayder


====================================================================================================

    Main configuration file

***************************************************************************************************/



var config = {

    countdown: {
        year: 2014,
        month: 6,
        day: 1,
        hours: 12,
        minutes: 0,
        seconds: 0
    },

    subscription_form_tooltips: {

        /* On successful subscription */
        success: "Cám ơn bạn đã đăng ký dùng thử HayHayBlog!",

        /* On some unknown error */
        default_error: "Error! Please, contact administration.",

        /* When email field is empty */
        empty_email: "Please, enter your email.",

        /* When email is invalid (for example, there is no @ character in it) */
        invalid_email: "Email is invalid. Please, enter valid email address.",

        /* When submitted email is already on the list */
        already_subscribed: "You are already subscribed."
    }
}