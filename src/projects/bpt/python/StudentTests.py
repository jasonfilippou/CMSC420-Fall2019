'''
Created on Jun 21, 2019

@author: AresShackleford
'''
import unittest, random
from BinaryPatriciaTrie import BinaryPatriciaTrie

class Test(unittest.TestCase):
    
    def testJunk(self):
        trie = BinaryPatriciaTrie()
        
        for x in range(1000):
            trie.insert(bin(random.randint(1,1000)))
            
        self.assertTrue(trie.is_Junk_Free(), "Trie is not junk Free")

    def testEmptyTrie(self):
        trie = BinaryPatriciaTrie()
        
        self.assertTrue(trie.is_Empty(), "Trie should be empty")
        self.assertTrue(trie.get_Size() == 0, "Trie size should be 0")
        self.assertFalse(trie.search("0101"), "No string inserted so search should fail")
        
    def testFewInsertionsWithSearch(self):
        trie = BinaryPatriciaTrie()
        
        self.assertTrue(trie.insert("00000"), "String should be inserted successfully")
        self.assertTrue(trie.insert("00011"), "String should be inserted successfully")
        self.assertFalse(trie.search("000"), "Search should fail as string does not exist")
        
    def testMoreInsertions(self):
        trie = BinaryPatriciaTrie()
        
        trie.insert("001"); 
        trie.insert("00100"); 
        trie.insert("11"); 
        trie.insert("1101"); 
        trie.insert("1111000"); 
        trie.insert("111100110001"); 
        
        self.assertTrue(trie.insert("111100110010"))
        
        
    def testLongest(self):
        trie = BinaryPatriciaTrie()
         
        trie.insert("010"); 
        trie.insert("001"); 
        
        self.assertTrue(trie.get_Longest() == "010", "Get longest is incorrect")
        
    def testtraversal(self):
        trie = BinaryPatriciaTrie()
         
        self.assertTrue(trie.get_Longest() == "", "Get longest is incorrect")
         
        trie.insert("0100001")
        trie.insert("010010")
        trie.insert("10")
        trie.insert("10")
        trie.insert("110")
        trie.insert("011")
        trie.insert("110")
        trie.insert("0100")
        trie.insert("0100010")
        
        itr = trie.in_Order_Traversal()
        
        for x in itr:
            print x
        
        self.assertTrue(trie.get_Size() == 7, "Size is not correct")
        self.assertTrue(trie.get_Longest() == "0100010", "Check your get Longest Method")
        
    def testFewInsertionsWithDeletion(self):
        trie = BinaryPatriciaTrie()
        
        trie.insert("000"); 
        trie.insert("001"); 
        trie.insert("011"); 
        trie.insert("1001"); 
        trie.insert("1"); 
        
        self.assertFalse(trie.is_Empty(), "After inserting five strings, the trie should not be considered empty!")
        self.assertEquals(5, trie.get_Size(), "After inserting five strings, the trie should report five strings stored.")
        trie.delete("0")
        
        self.assertEquals(5, trie.get_Size(), "After inserting five strings and requesting the deletion of one not in the trie, the trie " + "should report five strings stored.")
        self.assertTrue(trie.is_Junk_Free(), "After inserting five strings and requesting the deletion of one not in the trie, the trie had some junk in it!")
        
        trie.delete("011")
        self.assertEquals(4, trie.get_Size(), "After inserting five strings and deleting one of them, the trie should report 4 strings.")
        self.assertTrue(trie.is_Junk_Free(), "After inserting five strings and deleting one of them, the trie had some junk in it!")
        
if __name__ == "__main__":
    unittest.main()