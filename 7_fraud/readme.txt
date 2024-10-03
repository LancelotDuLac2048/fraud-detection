Programming Assignment 7: Fraud Detection

/* *****************************************************************************
 *  Describe how you implemented the Clustering constructor
 **************************************************************************** */

    - add edge between every two locations
    - compute MST of the graph
    - form a cluster graph with the lowest-weighted m-k edges
    - identify and categorize all clusters using the CC datatype

/* *****************************************************************************
 *  Describe how you implemented the WeakLearner constructor
 **************************************************************************** */

    - Iterated through all possible combinations of dp, sp, and vp
    - check if prediction is equal to predicted value
    - calculate the correpsonding weighted accuracy
    - keep a champ for weighted accuracy, update vp, sp, dp accordingly.
    - Maximize champ.

/* *****************************************************************************
 *  Consider the large_training.txt and large_test.txt datasets.
 *  Run the boosting algorithm with different values of k and T (iterations),
 *  and calculate the test data set accuracy and plot them below.
 *
 *  (Note: if you implemented the constructor of WeakLearner in O(kn^2) time
 *  you should use the small_training.txt and small_test.txt datasets instead,
 *  otherwise this will take too long)
 **************************************************************************** */

    Timing test done with small_training.txt and small_test.txt datasets:

      k          T         test accuracy       time (seconds)
   --------------------------------------------------------------------------
      1         100          0.55                0.181
      2         100          0.6875              0.356
      4         100          0.875               0.687
      8         100          0.86                1.288
      16        100          0.975               2.025
      17        100          0.9625              2.833

     (pick k = 16)
      16        10           0.95                0.309
      16        20           0.9375              0.42
      16        40           0.975               1.171
      16        80           0.975               1.25
      16        160          0.9625              2.927
      16        320          0.975               5.682
      16        640          0.975               14.099 (overtime)
      16        500          0.975               9.17

      (optimal: k = 16, T = 500)

/* *****************************************************************************
 *  Find the values of k and T that maximize the test data set accuracy,
 *  while running under 10 second. Write them down (as well as the accuracy)
 *  and explain:
 *   1. Your strategy to find the optimal k, T.
 *   2. Why a small value of T leads to low test accuracy.
 *   3. Why a k that is too small or too big leads to low test accuracy.
 **************************************************************************** */

    optimal: k = 16, T = 500
    - for a large value of iteration times, change k to find the optimal k.
    - fix k, change T and find the value of T yielding the highest accuracy.
    - keep runtime under 10 sec.

/* *****************************************************************************
 *  Known bugs / limitations.
 *************************************************************************** */

    Efficient implementation for weakLearner still not done. My attempts are shown
    below the weak Learner class (in the comments). That one passed all the timing
    tests but failed two of the correctness tests, but I think the logic is almost
    correct.

/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */

    I misread the course syllabus and submitted the last assignment a week late.
    This created an unfortunate compound effect on my scores, since I submitted
    this assignment late as well. Wrote to professor Paredes and hope with my
    fingers and toes and hairs crossed.

/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */

    Assignments deadlines tripped me up! I'm probably getting a B because of that,
    but it's a fun class altogether, learned a lot, struggled a little, will try
    to read syllabus and keep track of work better next time.

    Thank you to whoever's reading this, hope you had a good semester :)
