import unittest
import random
from AVLG_Tree import AVLGTree
from exception import InvalidBalanceException, EmptyTreeException


class StudentTest(unittest.TestCase):
    
    def test_searches(self):
        tree = AVLGTree(1)
        
        tree.insert(1)
        tree.insert(2)
        tree.insert(3)
        tree.insert(0)
        tree.insert(-2)
        tree.insert(10)
        
        self.assertEqual(tree.search(5), None)
        self.assertEqual(tree.search(10), 10)
         
        tree.delete(10)
        
        self.assertEqual(tree.search(10), None)
        
        tree.clear()
        
        with self.assertRaises(EmptyTreeException):
            tree.search(1)

    def test_simple_insert1(self):
        with self.assertRaises(InvalidBalanceException):
            tree = AVLGTree(0)
            tree.insert(1)
        
        tree = AVLGTree(1)
        
        tree.insert(8)
        tree.insert(2)
        tree.insert(30)
        tree.insert(4)
        tree.insert(10)
        tree.insert(20)
        
        self.assertEqual(tree.get_height(), 2, "Failed rotation at 30")
        
        tree.insert(15)
        tree.insert(22)
        tree.insert(19)
        
        self.assertEqual(tree.get_height(), 3, "Failed rotation at 10")
        
        tree.insert(17)
        
        self.assertEqual(tree.get_root(), 15, "Failed right left rotation")
        
    def test_simple_insert2(self):
        tree = AVLGTree(2)
        
        tree.insert(8)
        tree.insert(2)
        tree.insert(30)
        tree.insert(4)
        tree.insert(10)
        tree.insert(20)
        tree.insert(15)
        
        self.assertEqual(tree.get_height(), 3, "Failed left right rotation")
        
        tree.insert(22)
        tree.insert(19)
        tree.insert(17)
        
        self.assertEqual(tree.get_height(), 4, "Failed rotation at 10")
        
    def test_simple_insert3(self):
        tree = AVLGTree(3)
        
        tree.insert(8)
        tree.insert(2)
        tree.insert(30)
        tree.insert(4)
        tree.insert(10)
        tree.insert(20)
        tree.insert(15)
        tree.insert(22)
        tree.insert(19)
        
        self.assertEqual(tree.get_height(), 4, "Failed left right rotation")
        
        tree.insert(17)
        self.assertEqual(tree.get_height(), 5, "Failed insertion")
        
    def test_simple_delete4(self):
        tree = AVLGTree(4)
         
        tree.insert(100)
        tree.insert(110)
        tree.insert(120)
        tree.insert(50)
        tree.insert(20)
        tree.insert(80)
        tree.insert(10)
        tree.insert(5)
        tree.insert(15)
        tree.insert(60)
        tree.insert(55)
        tree.insert(62)
        tree.insert(65)
        tree.insert(63)
        tree.insert(70)
        
        tree.delete(120)
        
        self.assertEqual(tree.get_root(), 80, "Failed Deletion")
        self.assertEqual(tree.get_count(), 14, "Failed count")
        self.assertEqual(tree.get_height(), 5, "Failed height test")

    def test_simple_delete5(self):
        tree = AVLGTree(5)
         
        tree.insert(100)
        tree.insert(110)
        tree.insert(120)
        tree.insert(50)
        tree.insert(20)
        tree.insert(80)
        tree.insert(10)
        tree.insert(5)
        tree.insert(15)
        tree.insert(60)
        tree.insert(55)
        tree.insert(62)
        tree.insert(65)
        tree.insert(63)
        tree.insert(70)
        
        tree.delete(120)
        
        self.assertEqual(tree.get_root(), 100, "Failed Deletion")
        self.assertEqual(tree.get_count(), 14, "Failed count")
        self.assertEqual(tree.get_height(), 6, "Failed height test")
            
    def test_BST(self):
        tree = AVLGTree(3)
        
        for x in range(1000):
            tree.insert(random.randint(1,100))
        
        self.assertTrue(tree.is_BST(), "BST property failed")
        
    def test_AVLG(self):
        tree = AVLGTree(3)
        
        for x in range(1000):
            tree.insert(random.randint(1,100))
        
        self.assertTrue(tree.is_AVLG_Balanced(), "AVLG property failed")

    def test_multiple_deletions(self):
        tree = AVLGTree(1)
        
        tree.insert(3)
        tree.insert(4)
        tree.insert(5)
        tree.insert(2)
        tree.insert(0)
        tree.insert(12)
        tree.insert(1)
        
        tree.delete(12)
        
        self.assertEqual(tree.get_root(), 2, "Failed Deletion")
        self.assertEqual(tree.get_count(), 6, "Failed count")
        self.assertEqual(tree.get_height(), 2, "Failed height test")

        tree.delete(4)
        tree.delete(2)

        self.assertEqual(tree.get_root(), 3, "Failed Deletion")
        self.assertEqual(tree.get_count(), 4, "Failed count")
        self.assertEqual(tree.get_height(), 2, "Failed height test")

    def test_delete_root(self):
        tree = AVLGTree(4)
        
        tree.insert(100)
        tree.insert(110)
        tree.insert(120)
        tree.insert(50)
        tree.insert(20)
        tree.insert(80)
        tree.insert(10)
        tree.insert(5)
        tree.insert(15)
        tree.insert(60)
        tree.insert(55)
        tree.insert(62)
        tree.insert(65)
        tree.insert(63)
        tree.insert(70)
        
        self.assertEqual(tree.get_root(), 100)
        tree.delete(100)
        
        self.assertEqual(tree.get_count(), 14)
        self.assertEqual(tree.get_root(), 80)
        self.assertEqual(tree.get_height(), 5)

    def test_single_rotations(self):
        tree = AVLGTree(1)
        
        tree.insert(1)
        self.assertEqual(tree.get_root(), 1)
        
        tree.insert(2)
        tree.insert(3)
        
        # left rotation
        self.assertEqual(tree.get_root(), 2)
        
        tree.insert(0)
        tree.delete(3)
        
        # right rotation
        self.assertEqual(tree.get_root(), 1)
    
    def test_multiple_rotations(self):
        tree = AVLGTree(1)
        
        tree.insert(1)
        self.assertEqual(tree.get_root(), 1)
        
        tree.insert(4)
        tree.insert(3)
        
        # right left rotation
        self.assertEqual(tree.get_root(), 3)
        
        tree.insert(2)
        tree.delete(4)
        
        # left right rotation
        self.assertEqual(tree.get_root(), 2)
        
    def test_delete_and_clear(self):
        tree = AVLGTree(1)
        
        tree.insert(8)
        tree.insert(2)
        tree.insert(30)
        tree.insert(4)
        tree.insert(10)
        tree.insert(20)
        
        self.assertEqual(None, tree.delete(15))
        tree.clear()
        
        self.assertEqual(tree.get_count(), 0)
        self.assertTrue(tree.is_empty())
        
        with self.assertRaises(EmptyTreeException):
            tree.delete(10)


if __name__ == '__main__':
    unittest.main()
