####################################
Goibibo Business WebView Integration
####################################

This branch contains the sources to create our WebView Integration docs hosted
by Github Pages (using the gh-pages branch method). The below instructions
explain how to update & publish the final HTML.


First, clone this repository against the ``doc-source`` branch,

.. code:: bash

    $ cd ~
    $ git clone -b doc-source https://github.com/goibibo/GoibiboBusinessDemoApp.git
    $ cd GoibiboBusinessDemoApp

Next, create a Python3-based virtualenv, activate it and install the
dependencies mentioned in ``requirements.txt`` as shown below,

.. code:: bash

    $ virtualenv --python=python3 VENV
    $ source VENV/bin/activate
    (VENV)$ pip install -r requirements.txt

You can now edit/update the documentation within the ``source`` folder. We'll
recommend doing the editing with a separate shell environment. When you are
ready, return to this shell session and run the following command to generate
the HTML sources. They get created within the build/html folder - you can open
them locally using your web-browser.

.. code:: bash

    (VENV)$ make html

All that's left is to commit the files to our git repository and push the same
as a sub-tree to the ``gh-pages`` branch for publishing. Since we've finished
generating the HTML, let's deactivate the virtualenv too. The instructions are
as follows,

.. code:: bash

    (VENV)$ deactivate
    $ git add build/html
    $ git commit -m 'updating documentation'
    $ git subtree push --prefix build/html origin gh-pages

You can use the following link to view the published HTML documentation on
your web-browser;

https://goibibo.github.io/GoibiboBusinessDemoApp/

