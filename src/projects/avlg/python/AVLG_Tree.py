from exception import InvalidBalanceException, EmptyTreeException


class Node():
    def __init__(self, value):
        self.value = value
        self.left = None
        self.right = None
        self.height = 0
"""

<p>An <tt>AVL-G Tree</tt> is an AVL Tree with a relaxed balance condition. 
Its constructor receives a strictly positive parameter which controls 
the <b>maximum</b> imbalance allowed on any subtree of the tree which it creates. 
So, for example:</p> 
    <ul>
        <li>An AVL-1 tree is a classic AVL tree, which only allows for perfectly balanced 
        binary subtrees (imbalance of 0 everywhere), or subtrees with a maximum imbalance 
        of 1 (somewhere).</li> 
        <li>An AVL-2 tree relaxes the criteria of AVL-1 trees, by also allowing for 
        subtrees that have an imbalance of 2.</li> 
        <li>AVL-3 trees allow an imbalance of 3</li> <li>...</li> 
    </ul> 
    
    <p>The idea behind AVL-G trees is that rotations cost time, so maybe we would be 
    willing to accept bad search performance now and then if it would mean less rotations.</p> 
    
    @author <a href="https://github.com/JasonFil">Jason Filippou</a> 
"""

class AVLGTree():
    
    """
    The class constructor provides the tree with its maximum maxImbalance allowed.
    @param maxImbalance The maximum maxImbalance allowed by the AVL-G Tree. 
    @throws InvalidBalanceException if <tt>maxImbalance</tt> is a value smaller than 1.
    """
    def __init__(self, maxImbalance):
        if maxImbalance < 1:
            raise InvalidBalanceException("Value is smaller than 1")
        else:
            self.__size = 0
            self.__max = maxImbalance
            self.__root = None
            
    """
    Insert <tt>key</tt> in the tree. 
    @param key The key to insert in the tree.
    """
    def insert(self, key):
        self.__root = self.__insert_aux(self.__root, key)
    
    def __insert_aux(self, n, data):
        if n == None:
            self.__size += 1
            return Node(data)
        elif data != n.value:
            if data < n.value:
                n.left = self.__insert_aux(n.left, data)
            elif data > n.value:
                n.right = self.__insert_aux(n.right, data)
                
            n.height = 1 + max(self.__height(n.left), self.__height(n.right))
            return self.__balance_tree(n, self.__get_balance(n))
          
    #Finds the min value starting at node n  
    def __min(self, n):
        curr = n
        
        while curr.left != None:
            curr = curr.left
            
        return curr
    
    #Balances the tree at node n if needed
    def __balance_tree(self, n, balance):
        #imbalance on left side
        if balance > self.__max:
            #node deleted from right side or added to left side
            if self.__get_balance(n.left) >= 0:
                return self.__rotate_right(n)
                
            #node deleted from left side or added to the right side
            else:
                n.left = self.__rotate_left(n.left)
                return self.__rotate_right(n)
                    
        #imbalance on right side
        elif balance < -1 * self.__max:
            #node deleted from right side or added to the left side
            if self.__get_balance(n.right) > 0:
                n.right = self.__rotate_right(n.right)
                return self.__rotate_left(n)
                
            #node deleted from left side or added to the right side
            else:
                return self.__rotate_left(n)
            
        #no imbalance 
        else:
            return n
    
    """
    Delete the key from the data structure and return it to the caller. 
    @param key The key to delete from the structure. 
    @return The key that was removed, or <tt>null</tt> if the key was not found.
    @throws EmptyTreeException if the tree is empty.
    """
    def delete(self, key):
        if self.is_empty():
            raise EmptyTreeException("Delete failed")
        else:
            if self.search(key) == key:
                self.__root = self.__delete_aux(self.__root, key)
                self.__size -= 1
                return key
            else:
                return None
            
    def __delete_aux(self, n, data):
        if n == None:
            return n
        else:
            if data < n.value:
                n.left = self.__delete_aux(n.left, data)
            elif data > n.value:
                n.right = self.__delete_aux(n.right, data)
            else:
                if n.left == None or n.right == None:
                    if n.left == None:
                        temp = n.right
                    else:
                        temp = n.left
                        
                    if temp == None:
                        return None
                    else:
                        n = temp
                    
                else:
                    temp = self.__min(n.right)
                    
                    n.value = temp.value
                    n.right = self.__delete_aux(n.right, temp.value)
                
            n.height = 1 + max(self.__height(n.left), self.__height(n.right))
            return self.__balance_tree(n, self.__get_balance(n))
           
    #Balance of any given node
    def __get_balance(self, n):
        if n == None:
            return -1
        else:
            return self.__height(n.left) - self.__height(n.right) 
        
    """
    <p>Search for <tt>key</tt> in the tree. Return a reference to it if it's in there, 
    or <tt>null</tt> otherwise.</p> 
    @param key The key to search for. 
    @return <tt>key</tt> if <tt>key</tt> is in the tree, or <tt>null</tt> otherwise. 
    """   
    def search(self, key):
        if self.is_empty():
            raise EmptyTreeException("Search failed")
        else:
            curr = self.__root
            
            while curr != None:
                if curr.value == key:
                    return key
                elif key < curr.value:
                    curr = curr.left
                else:
                    curr = curr.right
                    
            return None
    """
    Retrieves the maximum imbalance parameter. 
    @return The maximum imbalance parameter provided as a constructor parameter. 
    """
    def get_max_Imbalance(self):
        return self.__max
    """
    <p>Return the height of the tree. The height of the tree is defined as the length of the 
    longest path between the root and the leaf level. By definition of path length, a 
    stub tree has a height of 0, and we define an empty tree to have a height of -1.</p> 
    @return The height of the tree. If the tree is empty, returns -1. 
    """
    def get_height(self):
        return self.__height(self.__root)
    
    #returns height of any node
    def __height(self, n):
        if n == None:
            return -1
        else:
            return n.height
    """
    Query the tree for emptiness. A tree is empty iff it has zero keys stored. 
    @return <tt>true</tt> if the tree is empty, <tt>false</tt> otherwise. 
    """
    def is_empty(self):
        return self.__size == 0
    
    """
    Return the key at the tree's root nodes. 
    @return The key at the tree's root nodes. 
    @throws  EmptyTreeException if the tree is empty. 
    """
    def get_root(self):
        if self.is_empty():
            raise EmptyTreeException('getRoot failed')
        else:
            return self.__root.value
    """
    <p>Establishes whether the AVL-G tree <em>globally</em> satisfies the BST condition. 
    This method is <b>terrifically useful for testing!</b></p> 
    @return <tt>true</tt> if the tree satisfies the Binary Search Tree property, 
    <tt>false</tt> otherwise. 
    """   
    def is_BST(self):
        return self.__BST(self.__root)
    
    def __BST(self, n):
        if n == None:
            return True
        else:
            return ((n.left == None or n.value > n.left.value) and 
                (n.right == None or n.value <= n.right.value) and
                self.__BST(n.left) and self.__BST(n.right))
            
    """
    <p>Establishes whether the AVL-G tree <em>globally</em> satisfies the AVL-G condition. 
    This method is <b>terrifically useful for testing!</b></p> 
    @return <tt>true</tt> if the tree satisfies the Binary Search Tree property, 
    <tt>false</tt> otherwise. 
    """
    def is_AVLG_Balanced(self):
        return self.__is_Balanced(self.__root)
    
    def __is_Balanced(self, n):
        if n == None:
            return True
        else:
            lh = self.__height(n.left)
            rh = self.__height(n.right)
            
            return (abs(lh - rh) <= self.__max and self.__is_Balanced(n.left) and 
                self.__is_Balanced(n.right))    
    """
    <p>Empties the <tt>AVLGTree</tt> of all its elements. After a call to this method, the 
    tree should have <b>0</b> elements.</p> 
    """
    def clear(self):
        self.__size = 0
        self.__root = None
    
    """
    <p>Return the number of elements in the tree.</p> 
    @return  The number of elements in the tree. 
    """
    def get_count(self):
        return self.__size
    
    def __rotate_right(self, n):
        temp = n.left
        r = temp.right
        
        temp.right = n
        n.left = r
        
        n.height = 1 + max(self.__height(n.left), self.__height(n.right))
        temp.height = 1 + max(self.__height(temp.left), self.__height(temp.right))
        
        return temp
        
    def __rotate_left(self, n):
        temp = n.right
        r = temp.left
        
        temp.left = n
        n.right = r
        
        n.height = 1 + max(self.__height(n.left), self.__height(n.right))
        temp.height = 1 + max(self.__height(temp.left), self.__height(temp.right))
        
        return temp