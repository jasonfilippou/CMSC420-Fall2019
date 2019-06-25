"""
<p> <tt>InvalidBalanceException</tt> is an {@link Exception} that is thrown by the constructor 
of {@link projects.avlg.AVLGTree} 

<p>You should <b>not</b> edit this class! It is given to you as a resource for your project.</p> 

@author <a href="https://github.com/jasonfil">Jason Filippou</a> 

@see EmptyTreeException 
"""


class InvalidBalanceException(Exception):
    def __init__(self, message):
        super(InvalidBalanceException, self).__init__(message)