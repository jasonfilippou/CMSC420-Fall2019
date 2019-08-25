package homeworks.hw1;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.IntStream;

/**
 * A client program for {@link MyStack}. Use it to answer your homework questions.
 *
 * @author <a href="github.com/JasonFil">Jason Filippou</a>
 *
 * @see MyStack
 */
public class MyStackClient {

    private static int tryBlockNum = 1;

    private static void printTryBlockHeader(){
        System.out.println("\n------------");
        System.out.println("TRY BLOCK " + (tryBlockNum++));
        System.out.println("------------\n");
    }

    private static String throwableInfo(Throwable thrown){
        return "Caught a " + thrown.getClass().getSimpleName() +
                " with message: " + thrown.getMessage();
    }

    /**
     * Runner method of application.
     * @param args Command line arguments.
     */
    public static void main(String[] args){

        /* TRY BLOCK 1: What will this try - catch block print? In all questions,
          * disregard the output of printTryBlockHeader().
          */
        printTryBlockHeader();
        try {
            MyStack<Object> stack = new MyStack<>(0, StackSize.SMALL);
            System.out.println("Nothing thrown!");
        } catch(Throwable thrown){
            System.out.println(throwableInfo(thrown));
        }

        /* TRY BLOCK 2: What will this print? */
        printTryBlockHeader();
        try {
            MyStack<Integer> stack = new MyStack<>(10, StackSize.SMALL);
            IntStream.rangeClosed(1, 11).forEach(stack::push);
            System.out.println("Nothing thrown!");
        } catch(Throwable thrown){
            System.out.println(throwableInfo(thrown));
        }

        /* TRY BLOCK 3: What will this print? */
        printTryBlockHeader();
        try {
            MyStack<Integer> stack = new MyStack<>(10, StackSize.LARGE);   // Large stack
            IntStream.rangeClosed(1, 11).forEach(stack::push);
            System.out.println("Nothing thrown!");
        } catch(Throwable thrown){
            System.out.println(throwableInfo(thrown));
        }

        /* TRY BLOCK 4: What will this print? */
        printTryBlockHeader();
        try {
            MyStack<Integer> stack = new MyStack<>(10, StackSize.SMALL);   // Large stack
            IntStream.rangeClosed(1, 10).forEach(stack::push);
            System.out.println("Nothing thrown!");
        } catch(Throwable thrown){
            System.out.println(throwableInfo(thrown));
        }

        /* TRY BLOCK 5: In a fail-fast Iterator (which you have to implement for MyStack!)
         * what *should* the following output be?
         */
        printTryBlockHeader();
        try {
            MyStack<Integer> stack = new MyStack<>(10, StackSize.SMALL);
            Arrays.asList(10, 20, 15, 42).forEach(stack::push);
            Iterator<Integer> it = stack.iterator();
            for(int i = 0; i < 2; i++)
                System.out.println(it.next());
            System.out.println(it.hasNext());
            System.out.println(stack.pop());
            System.out.println(it.next());
            System.out.println("Nothing thrown!");
        } catch(Throwable thrown){
            System.out.println(throwableInfo(thrown));
        }

        /* "TRY BLOCK 6" (we do not actually write a new code block, since you have to answer this
         * question by inspecting the same try block as above): In a fail-*safe* Iterator (which you *might*
         * want to implement for MyStack!) what *should* the *same code*  output?
         */
        tryBlockNum++;

        /* TRY BLOCK 7: In an Iterator which does *not* implement remove(),
         * what *should* the following output?
         */
        printTryBlockHeader();
        try {
            MyStack<Integer> stack = new MyStack<>(10, StackSize.SMALL);
            Arrays.asList(10, 20, 15, 42).forEach(stack::push);
            Iterator<Integer> it = stack.iterator();
            for(int i = 0; i < 2; i++)
                System.out.println(it.next());
            System.out.println(it.hasNext());
            it.remove();
            System.out.println(it.next());
            System.out.println("Nothing thrown!");
        } catch(Throwable thrown){
            System.out.println(throwableInfo(thrown));
        }

        /* "TRY BLOCK 8": In the same code block as above, what should the output be if we *do*
         * implement remove() in our Iterator?
         */
        tryBlockNum++;

        /* TRY BLOCK 9: For an Iterator that *does* implement remove(), what will the output
         * of the following try - block be?
         */
        printTryBlockHeader();
        try {
            MyStack<Integer> stack = new MyStack<>(10, StackSize.SMALL);
            Arrays.asList(10, 20, 15, 42).forEach(stack::push);
            Iterator<Integer> it = stack.iterator();
            it.remove();
            for(int i = 0; i < 3; i++)
                System.out.println(it.next());
            System.out.println("Nothing thrown!");
        } catch(Throwable thrown){
            System.out.println(throwableInfo(thrown));
        }

        /* TRY BLOCK 10: For *either* a fail-safe *or* a fail-fast Iterator, what should
        the output of the following code be?  */
        printTryBlockHeader();
        try {
            MyStack<Integer> stack = new MyStack<>(10, StackSize.SMALL);
            Arrays.asList(10, 20, 15, 42).forEach(stack::push);
            Iterator<Integer> it = stack.iterator();
            for(int i = 0; i < 4; i++)
                System.out.println(it.next());
            System.out.println(it.hasNext());
            System.out.println(it.next());
            System.out.println("Nothing thrown!");
        } catch(Throwable thrown){
            System.out.println(throwableInfo(thrown));
        }
    }
}
