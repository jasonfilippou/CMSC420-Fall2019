'''
Created on Jun 21, 2019

@author: Ares Shackleford
'''
class Node():
    def __init__(self, keyRef, isKey):
        self.left = None
        self.right = None
        self.keyRef = keyRef
        self.isKey = isKey
"""
<p>{@link BinaryPatriciaTrie} is a Patricia Trie over the binary alphabet &#123;      
0, 1 &#125;. By restricting themselves to this small but terrifically useful alphabet, 
Binary Patricia Tries combine all the positive aspects of Patricia Tries while shedding 
the storage cost typically associated with Tries that deal with huge alphabets.</p>
"""   
class BinaryPatriciaTrie():
    
    """
    Simple constructor that will initialize the internals of this. 
    """
    def __init__(self):
        self.__size = 0
        self.__root = Node("", False)
    
    """
    Searches the trie for a given key.
    @param key The input String key.
    @return true if and only if key is in the trie, false otherwise.
    """
    def search(self, key):
        return self.__search_aux(self.__root, key)

    def __search_aux(self, n, key):
        if n is None or len(n.keyRef) > len(key):
            return False
        elif len(n.keyRef) == len(key):
            if n.isKey and n.keyRef == key:
                return True
            return False
        else:
            prefix = self.__compute_pre(key, n.keyRef)
            
            if key[len(prefix)] == "0":
                return self.__search_aux(n.left, key[len(prefix):])
            return self.__search_aux(n.right, key[len(prefix):]) 
    """
    Inserts key into the trie. 
    @param key The input String key. 
    @return true if and only if the key was not already in the trie, false otherwise. 
    """
    def insert(self, key):
        if self.search(key):
            return False
        
        self.__root = self.__insert_aux(self.__root, key)
        self.__size += 1
        
        return True
        
    def __insert_aux(self, n, key):
        if n is None:
            n = Node(key, True)
        elif n.keyRef == key:
            n.isKey = True
        elif n.keyRef == self.__compute_pre(key, n.keyRef):
            if key[len(n.keyRef)] == "0":
                n.left = self.__insert_aux(n.left, key[len(n.keyRef):])
            else:
                n.right = self.__insert_aux(n.right, key[len(n.keyRef):])
        elif key == self.__compute_pre(key, n.keyRef):
            last = n.keyRef[len(key):]
            parent = Node(key,True)
            n.keyRef = last 
            
            if last[0] == "0":
                parent.left = n
            else:
                parent.right = n
            n = parent
        else:
            parent = Node(self.__compute_pre(key, n.keyRef), False)      
            n.keyRef = n.keyRef[len(parent.keyRef):]  
                 
            if n.keyRef[0] == "0":
                parent.left = n
            else:
                parent.right = n
            
            self.__insert_aux(parent, key)
            n = parent
        return n        
        
    """
    Deletes key from the trie. 
    @param key The String key to be deleted.
    @return True if and only if key was contained by the trie before we attempted deletion, 
    false otherwise. 
    """
    def delete(self, key):
        if not self.search(key):
            return False
        
        self.__root = self.__delete_aux(self.__root, key)
        self.__size -= 1
        
        self.__fix_Junk(self.__root)
        return True
    
    def __delete_aux(self, n, key):
        if n.keyRef == key:
            if n.left != None and n.right != None:
                n.isKey = False
            elif n.left is None and n.right is None:
                n = None
            else:
                if n.left != None:
                    self.__merge(n,n.left)
                else:
                    self.__merge(n,n.right)   
                    
        elif n.keyRef == self.__compute_pre(key, n.keyRef):
            if key[len(n.keyRef)] == "0":
                n.left = self.__delete_aux(n.left, key[len(n.keyRef):])
            else:
                n.right = self.__delete_aux(n.right, key[len(n.keyRef):])
        return n
    
    def __compute_pre(self, key, ref):
        pre = ""
        
        if len(key) > len(ref):
            length = len(ref)
        else:
            length = len(key)
        
        for i in range(length):
            if key[i] == ref[i]:
                pre += key[i]
            else:
                break
    
        return pre
    
    def __merge(self, parent, child):
        if child is not None:
            parent.keyRef += child.keyRef
            parent.isKey = child.isKey
            parent.left = child.left
            parent.right = child.right
        else:
            raise AssertionError("__merge(): cannot have a null child")
        
    def __fix_Junk(self, n):
        if n is not None:
            self.__fix_Junk(n.left)
            if n.isKey is False and (n.left is None or n.right is None) and n != self.__root:
                if n.right is None:
                    self.__merge(n, n.left)
                else:
                    self.__merge(n, n.right)
            self.__fix_Junk(n.right)    
    """
    Queries the trie for emptiness. 
    @return true if and only if {@link #getSize()} == 0, false otherwise. 
    """
    def is_Empty(self):
        return self.__size == 0
    
    """
    Returns the number of keys in the tree. 
    @return The number of keys in the tree. 
    """
    def get_Size(self):
        return self.__size
    
    """
    <p>Performs an <i>inorder (symmetric) traversal</i> of the Binary Patricia Trie. 
    Remember from lecture that inorder traversal in tries is NOT sorted traversal, 
    unless all the stored keys have the same length. This is of course not required by your 
    implementation, so you should make sure that in your tests you are not expecting this 
    method to return keys in lexicographic order. We put this method in the interface because it 
    helps us test your submission thoroughly and it helps you debug your code! </p> 
    
    <p>We <b>neither require nor test </b> whether the {@link Iterator} returned by this 
    method is fail-safe or fail-fast. This means that you  do <b>not</b> need to test for 
    thrown {@link java.util.ConcurrentModificationException}s and we do <b>not</b> test your 
    code for the possible occurrence of concurrent modifications.</p> 
    
    <p>We also assume that the {@link Iterator} is <em>immutable</em>, i,e we do <b>not</b> 
    test for the behavior of {@link Iterator#remove()}. You can handle it any way you want 
    for your own application, yet <b>we</b> will <b>not</b> test for it.</p> 
    
    @return An {@link Iterator} over the {@link String} keys stored in the trie, 
    exposing the elements in <i>symmetric order</i>. 
    """
    def in_Order_Traversal(self):
        words = []
        
        self.__in_Order(self.__root, words, "")
        
        return words.__iter__()
    
    def __in_Order(self, n, words, word):
        if n != None:
            word += n.keyRef
            self.__in_Order(n.left, words, word)
            if n.isKey:
                words.append(word)
            self.__in_Order(n.right, words, word)
    """
    Finds the longest {@link String} stored in the Binary Patricia Trie. 
    
    @return <p>The longest {@link String} stored in this. If the trie is empty, 
    the empty string &quot;&quot; should be returned. Careful: the empty string 
    &quot;&quot;is <b>not</b> the same string as &quot; &quot;; the latter is a 
    string consisting of a single <b>space character</b>! It is also <b>not the 
    same as the</b> null <b>reference</b>!</p> 
    
    <p>Ties should be broken in terms of <b>value</b> of the bit string. For example, 
    if our trie contained only the binary strings 01 and 11, <b>11</b> would be the 
    longest string. If our trie contained only 001 and 010, <b>010</b> would be the 
    longest string.</p>
    """
    def get_Longest(self):
        length = 0
        word = ""
        
        if self.is_Empty():
            return self.__root.keyRef
        
        for i in self.in_Order_Traversal():
            if len(i) > length:
                word = i
                length = len(word)
            elif len(i) == length:
                if int(i, 2) > int(word,2):
                    word = i
        return word
    """
    Makes sure that your trie doesn't have splitter nodes with a single child. 
    In a Patricia trie, those nodes should be pruned. Be careful with the 
    implementation of this method, since our tests call it to make sure your 
    deletions work correctly! That is to say, if your deletions work well, 
    but you have made an error in this (far easier) method, you will <b>still</b> 
    not be passing our tests!
    
    @return true iff all nodes in the trie either denote stored strings or split 
    into two subtrees, false otherwise.
    """
    def is_Junk_Free(self):
        return self.__junk(self.__root.left) and self.__junk(self.__root.right)
    
    def __junk(self, n):
        if n is None:
            return True
        else:
            if n.isKey == False and (n.left is None or n.right is None):
                return False
            else:
                return self.__junk(n.left) and self.__junk(n.right)            