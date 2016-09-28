.. A comment

#################################################
GoibiboBusiness WebView Integration documentation
#################################################

The purpose of this documentation is to help our B2B partners integrate a
customized version of the goibibo website into their native mobile app to allow
end-users access to pricing and travel options they would not be able to find
elsewhere. A high-level overview of the end-user experience is visualized
below. Please refer to the links near the bottom of this page for specific
details about the different steps.

.. note::

   Depending on requirements, not all options are available for every
   provider - feel free to contact us for details!


.. actdiag::
   :desctable:

    actdiag {
      default_fontsize = 14;

      open-travel -> verify-user -> open-webview -> show-travel;
      show-travel -> select-travel -> initiate-pay -> pay-begin;
      pay-begin -> process-payment -> pay-confirm -> book-travel;
      book-travel -> show-ticket -> perform-travel;

      lane user {
         label = "End User"
         open-travel [description = "User selects travel option"];
         select-travel [description = "User finalizes travel plans"];
         initiate-pay [description = "User selects 'Make Payment' button"];
         show-ticket [description = "User sees ticket within WebView"];
         perform-travel [description = "User goes on travel"];
      }

      lane b2b-partner {
         label = "Mobile App"
         verify-user [description = "B2B-Partner associates user with unique identifier"];
         open-webview [description = "Login user on Goibibo via WebView API call"];
      }

      lane goibibo {
         label = "Goibibo server"
         show-travel [description = "Display travel options to User"];
         pay-begin [description = "Request amount deducted from user account"];
         pay-confirm [description = "After payment is confirmed, begin booking"];
         book-travel [description = "Finalize travel details with travel provider"];
      }

      lane payment {
         label = "Payment System"
         process-payment [description = "Process payment request"];
      }
    }



*******************
Integration Options
*******************

.. toctree::
   :maxdepth: 2

   initial_login


Indices and tables
==================

* :ref:`genindex`
* :ref:`modindex`
* :ref:`search`

