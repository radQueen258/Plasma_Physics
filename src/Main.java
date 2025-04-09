import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        int n = 10;
        double timeEnd = 30000000;
        double L = 0.7; // Wall thickness
        double h = L / n;
        double tau = 5000; // Time step (smaller for explicit stability)

        double[] x = new double[n+1];
        double[] A = new double[n+1];
        double[] B = new double[n+1];
        double[] C = new double[n+1];
        double[] F = new double[n+1];
        double[] y = new double[n+1];
        double[] y_old = new double[n+1];
        double[] lambda = new double[n+1];

        double y0 = -30; // Init temp.

//        Init arrays
        for (int i = 0; i <= n; i++) {
            x[i] = h * i;
            y[i] = 0;
            y_old[i] = y0;

//            CASE 1: Insulation inside (left side)

//            if (x[i] <= 0.35) {
//                lambda[i] = 0.045/50/85000; // Insulation
//            } else {
//                lambda[i] = 0.64/1600/840;  // Regular wall
//            }

//            CASE 2: Insulation outside (right side)
            if (x[i] <= 0.35) {
                lambda[i] = 0.64/1600/840; // Regular wall
            } else {
                lambda[i] = 0.045/50/85000; // Insulation
            }

        }
//            Boundary conditions
        double mu1 = 30, kappa1 = 0;
        double mu2 = -30, kappa2 = 0;

//            Time stepping
        for (double currentTime = 0; currentTime <= timeEnd;
             currentTime += tau) {
//                Choose your solver:
//            HeatConduction.solveImplicit(n, h, x, A, B, C, F, mu1, kappa1, mu2, kappa2,
//                    lambda, y, tau, y_old);

            HeatConduction.solveExplicit(n, h, lambda, y, tau, y_old);

//               Save results at spec. times
            if (Math.abs(currentTime - 3000) < tau/2 ||
                    Math.abs(currentTime - 3000) < tau/2 ||
                    Math.abs(currentTime - 30000) < tau/2 ||
                    Math.abs(currentTime - 3000000) < tau/2 ||
                    Math.abs(currentTime - 30000000) < tau/2 ) {
                HeatConduction.saveResults(String.format("results_%.0f.csv", currentTime), x, y);
            }
            //                Updating for the next time step
            System.arraycopy(y, 0, y_old, 0, n+1);
        }

        //    Final output
        for (int i = 0; i <= n; i ++) {
            System.out.printf("x=%.2f, T=%.2f\n", x[i], y[i]);
        }
    }
}