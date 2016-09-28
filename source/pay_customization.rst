*********************
Payment Customization
*********************

From an end-user's point-of-view, when they click the 'Make Payment' button
after finalizing a travel option, they expect to be taken to a screen which
gives them the option to pay. By default this will be the same, secure page
that is seen on https://www.goibibo.com/ . After payment is made, end-users
will expect to see details of their upcoming travel purchase.

In some cases, end-users might have accounts with our B2B-Partner which they
would like to pay from instead. To accomodate this, we provide the following
customization options,

.. note::

   Please use ``pp.goibibobusiness.com`` for development/testing purposes and
   ``www.goibibobusiness.com`` in the production environment.

Goibibo displays payment screens
================================

We recommend this approach as it keeps the end-user within the same WebView -
reducing the chances of a man-in-the-middle attack. Also, since payments are
conducted via server-to-server interactions, it minimizes the risk of fraud.
The below figure visualizes this flow,

.. seqdiag::

    seqdiag {
        default_fontsize = 14;
        Mobile-app; Goibibo; B2B-Partner;

        Mobile-app  ->  Goibibo [label = "User clicks 'make payment'"];
                        Goibibo  -> B2B-Partner [label = "request account details of user"];
                        Goibibo <-- B2B-Partner [label = "return data"];
        Mobile-app <--  Goibibo [label = "display payment options"];
        Mobile-app  ->  Goibibo [label = "confirm payment"];
        === Optional OTP Sequence Start ===
                        Goibibo  -> B2B-Partner [label = "send OTP to user"];
                        Goibibo <-- B2B-Partner [label = "OTP sent"];
        Mobile-app <--  Goibibo [label = "request for OTP"];
        Mobile-app  ->  Goibibo [label = "User enters OTP"];
        === Optional OTP Sequence End ===
                        Goibibo  -> B2B-Partner [label = "deduct funds from user's account"];
                        Goibibo <-- B2B-Partner [label = "return transaction details"];
                        Goibibo  -> Goibibo [label = "make travel\nbooking"];
        Mobile-app <--  Goibibo [label = "display purchased travel details"];
        Mobile-app  ->  Mobile-app [label = "Happy User\ngoes travelling"];
    }

To make use of the above flow, Goibibo needs corresponding APIs to be
available from our B2B-Partner.




