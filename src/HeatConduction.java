import java.io.FileWriter;
import java.io.IOException;

public class HeatConduction {
//    Thomas algorithm
    public static void solveMatrix (int n, double[] a, double[] b, double[] c, double[] f,
                                     double mu1, double kappa1, double mu2, double kappa2,
                                     double[] y) {
        double[] alpha = new double[n+1];
        double[] beta = new double[n+1];

        alpha[n] = kappa2;
        beta[n] = mu2;

//        Backward sweep
        for (int i = n-1; i >= 1; i --) {
            alpha[i] = a[i] / (c[i] - alpha[i+1] * b[i]);
            beta[i] = (b[i] * beta[i+1] + f[i]) / (c[i] - alpha[i+1] * b[i]);
        }

//        Forward substitution
        y[0] = (mu1 + kappa1 * beta[1]) / (1 - alpha[1] * kappa1);
        for (int i = 0; i < n; i++) {
            y[i+1] = alpha[i+1] * y[i] + beta[i+1];
        }

    }

    //        Implicit scheme solver
    public static void solveImplicit (int n, double h, double[] x, double[] A, double[] B,
                                       double[] C, double[] F, double mu1, double kappa1, double mu2,
                                       double kappa2, double[] lambda, double[] y, double tau, double[] y_old) {
        for (int i = 1; i < n; i++) {
            A[i] = - (lambda[i] + lambda[i-1]) / (2*h*h);
            B[i] = - (lambda[i] + lambda[i+1]) / (2*h*h);
            C[i] = A[i] + B[i] - 1 / tau;
            F[i] = - y_old[i] / tau;
        }
        solveMatrix(n, A, B, C, F, mu1, kappa1, mu2, kappa2, y);
    }

//    Explicit scheme solver
    public static void solveExplicit (int n, double h, double[] lambda, double[] y, double tau, double[] y_old) {
        double[] newY = new double[n+1];
        System.arraycopy(y_old, 0, newY, 0, n+1);

        for (int i = 1; i < n; i++) {
            double d = tau * (lambda[i] + lambda[i+1]) / (2*h*h);
            newY[i] = y_old[i] + d * (y_old[i+1] - 2 * y_old[i] + y_old[i-1]);
        }

//        Apply boundary conditions
        newY[0] = 30; // Left boundary
        newY[n] = -30; // Right boundary

        System.arraycopy(newY, 0, y, 0, n+1);
    }

//    Save the result to a CSV file
    public static void saveResults (String filename, double[] x, double[] y) throws IOException {
        FileWriter writer = new FileWriter(filename);
        for (int i = 0; i < x.length; i ++) {
            writer.write(String.format("%f,%f\n", x[i], y[i]));
        }
        writer.close();
    }
}
