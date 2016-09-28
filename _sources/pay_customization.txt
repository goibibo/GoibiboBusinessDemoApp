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


Request account details of user
-------------------------------

This is an API which should be implemented by our B2B-Partner. It is to be a
server-to-server call which will allow Goibibo to transmit ``user_id`` ,
``token`` . The API should return details about user's account balance which
Goibibo can show to end-user.


Send OTP to user
----------------

This is an API which should be implemented by our B2B-Partner. It is to be a
server-to-server call which will allow Goibibo to transmit ``user_id`` ,
``token`` and ``goibibo_txnid`` (the Goibibo-specific transaction ID). The API
should return ``success`` or ``true`` if an OTP message was transmitted to
end-user. Otherwise, it should return ``false`` or ``failed``.


Deduct funds from user's account
--------------------------------

This is an API which should be implemented by our B2B-Partner. It is to be a
server-to-server call which will allow Goibibo to transmit ``user_id`` ,
``token`` , ``goibibo_txnid`` (the Goibibo-specific transaction ID) ,
``amount`` (price to deduct) and ``otp_message`` (OTP sent to end-user,
if needed). The API should return ``ref_num`` (Partner specific payment ID),
``amount`` (amount actually deducted from end-user's account), ``pay_status``
(status of payment - was it success or failure?) and ``checksum`` (To verify
integrity of message)



B2B-Partner displays payment screens
====================================

We do not encourage this mechanism, since it exposes the risk of a man-in-the-middle
attack as well as to increase the delay between booking confirmation. However,
if a B2B-Partner is insistent on this approach here is a visualization of the
flow,

.. seqdiag::

    seqdiag {
        default_fontsize = 14;
        End-User; Mobile-app; Goibibo; B2B-Partner;

        End-User  ->                    Goibibo   [label = "User clicks 'make payment'"];
                        Mobile-app  <-  Goibibo   [label = "deduct funds from user's account"];
                        Mobile-app  ->  Mobile-app [label = "process payment"];
                        Mobile-app -->  Goibibo   [label = "return transaction details"];
                                        Goibibo  -> Goibibo [label = "Show landing page to user while transaction is processed"];
        === Recommended, but optional Verification Sequence Start ===
                                        Goibibo  -> B2B-Partner [label = "request transaction status"];
                                        Goibibo <-- B2B-Partner [label = "return data"];
        === Recommended, but optional Verification Sequence End ===
                                        Goibibo  -> Goibibo [label = "make travel\nbooking"];
        End-User <--                    Goibibo [label = "display purchased travel details"];
        End-User  ->    End-User [label = "Happy User\ngoes travelling"];
    }


Here are details of the APIs that need to be implemented for the above flow
to work,

Deduct funds from user's account
--------------------------------

.. note::

   Please replace ``example_bank`` with your organization-code

.. http:get:: http://pp.goibibobusiness.com/b2b_partner/example_bank/payment_external_process/
    :noindex:

    :query user_id: A string to uniquely identify a user
    :query token: The Token number to use when calling Partner APIs
    :query goibibo_txnid: Goibibo-specific transaction ID for selected trip
    :query amount: Price to deduct from user's account (might have decimal component eg: Rs 80.73/-)
    :query timestamp: Time request is made, format: YYYY-MM-DD HH:MM:SS
    :query checksum: SHA512 value to check integrity of request

    The Goibibo webapp will make a GET request to this end-point (NOTE: if
    partner wishes, we can change the URL). It is expected that instead of
    accessing a web-service, the mobile app will intercept this call and take
    over control flow till payment is processed.

    The checksum is generated by passing a **single string** to a SHA512
    function. This input string is created by appending the following data,

    user_id + token + goibibo_txnid + amount + timestamp + PAYMENT_KEY

.. note::

   Please contact us for PAYMENT_KEY


Return Transaction details to Goibibo
-------------------------------------

.. note::

   Please replace ``example_bank`` with your organization-code

.. http:post:: http://pp.goibibobusiness.com/b2b_partner/example_bank/payment_external_complete_partner_side/
    :noindex:

    :query user_id: A string to uniquely identify a user
    :query token: The Token number to use when calling Partner APIs
    :query goibibo_txnid: Goibibo-specific transaction ID for selected trip
    :query amount: Amount deducted from end-user's account
    :query timestamp: Time request is made, format: YYYY-MM-DD HH:MM:SS
    :query ref_num: Partner-specific payment ID
    :query checksum: SHA512 value to check integrity of request
    :query pay_status: A string to indicate status of transaction
    :query comment: A string which indicates cause of error (if needed)

    This is the API call to be made from mobile WebView to Goibibo after
    payment has been processed by B2B-Partner. If it was a success,
    ``pay_status`` should be '``success``'. Any other string is assumed to
    indicate failure. Also, if the transaction failed, ``amount`` should be
    '``0.0``' and ``comment`` should be a string explaining cause of failure.
    On successful payment, ``amount`` should be the same as the value
    requested (the API does not support partial payments).

    The checksum is generated by passing a **single string** to a SHA512
    function. This input string is created by appending the following data,

    user_id + token + goibibo_txnid + amount + timestamp + ref_num + PAYMENT_KEY

.. note::

   Please contact us for PAYMENT_KEY


Transaction Status API for Goibibo
----------------------------------

This is an API which should be implemented by our B2B-Partner. It is to be a
server-to-server call which will allow Goibibo to transmit ``goibibo_txnid``
(the Goibibo-specific transaction ID) and ``ref_num`` (Partner specific
payment ID). The API should return ``pay_status`` (Indicates if payment was
success or not), ``amount`` (Amount which was deducted from end-user's
account), ``comment`` (Reason for payment failure) and ``checksum`` (To verify
integrity of message).



