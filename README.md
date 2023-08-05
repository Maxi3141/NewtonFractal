# NewtonFractal
Interactive Java app to create Newton fractals.
!!!There is no guarantee that any of the shown results are correct or usefull!!!

Newton fractals are fractal that emerge from Newton's method for finding roots of a function f(z).
Iterate x_(n+1) = x_n - f(z) / f'(z). Depending on x_0 this will converge to different roots of f(z).
The fractal is then obtained by using different points in the complex plane as x_0 and coloring them in the same color if they converge to the same root.

How to use:
Enter your function in the "f(z)=" textfield. Supported operators are "+", "-", "*", "/", "^", "exp", "log", "cos" and "sin".
Be carefull that "z" is the variable name. If you use "x" you will get errors.
"Tolerance (root)" is the value below of which the function will be considered to have reached one of its roots.
"Tolerance (grouping)" is the smallest (euclidiean) distance two roots can be apart and still be considered two separate roots.
"MaxIter" is the number of iterations performed for each pixel. If the Newton algorithm has not converged to a root after maxIter steps, the pixel will be black.
The two "corner"-variables determine the region of the complex plane you are looking at. They have the form (real coordinate | imaginary coordinate).
After all these variables have been set, click "Refresh" to start generating the fractal. Print statements in the console will indicate the progress. 
For more complicated f(z) this can take a few seconds.
The main class is contained in "NewtonFractal.java".
