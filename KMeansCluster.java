import java.util.*;

class KMeansCluster {

    class Cluster {
        private List<Integer> data = new ArrayList<>();
        private double variance;
        private double mean;
        private int location;

        public Cluster(List<Integer> data) {
            this.data = data;
            setMean();
            setVariance();
        }

        public Cluster(int location) {
            this.location = location;
        }

        public List<Integer> getData() {
            return data;
        }

        public void addData(int data) {
            this.data.add(data);
            this.setMean();
            this.setVariance();
        }

        public int getLocation() {
            return location;
        }

        public void setLocation(int location) {
            this.location = location;
        }

        public double getVariance() {
            return variance;
        }

        public void setVariance() {
            double sumSqrDiffs = 0.0;
            for (int i = 0; i < this.data.size(); i++) {
                double diff = this.data.get(i) - this.getMean();
                double sqrDiff = Math.pow(diff, 2);
                sumSqrDiffs += sqrDiff;
            }
            this.variance = sumSqrDiffs / this.data.size() - 1;
        }

        public double getMean() {
            return mean;
        }

        public void setMean() {
            //some logic to get the mean
            int sumOfTerms = 0;
            for (int i = 0; i < this.data.size(); i++) {
                sumOfTerms += this.data.get(i);
            }
            //calculate the average
            this.mean = sumOfTerms / this.data.size();
        }

        @Override
        public String toString() {
            return this.data.toString() + " location=" + this.location + " variance=" + this.variance + " mean=" + this.mean + "\n";
        }
    }

    private List<Cluster> initalClusters(int min, int max, int clusters) {
        List<Cluster> initialClusters = new ArrayList<>();
        for (int i = 0; i < clusters; i++) {
            Random r = new Random();
            int location = Double.valueOf((Math.random() * ((max - min) + 1)) + min).intValue();
            Cluster cluster = new Cluster(location);
            initialClusters.add(cluster);
        }
        return initialClusters;
    }

    public Map<String, List<Cluster>> kMeans(int[] dataSet) {
        //will store index of closest cluster
        int closestCluster = -1;
        List<Cluster> initialClusters = null;

        Map<String, List<Cluster>> results = new HashMap<>();
        //algorithm tries 10 clusters and pick best one
        for (int i = 1; i <= 10; i++) {
            //get initial clusters
            initialClusters = initalClusters(0, dataSet[dataSet.length - 1], i);
            //for each point in the set
            for (int j = 0; j < dataSet.length; j++) {
                //measure distance to each cluster
                for (int k = 0; k < initialClusters.size(); k++) {
                    if (closestCluster == -1) {
                        closestCluster = k;
                    }
                    int currentClusterDist = Math.abs(dataSet[j] - initialClusters.get(k).location);
                    //check if current cluster is closer than the closest cluster
                    if (currentClusterDist <= Math.abs(dataSet[j] - initialClusters.get(closestCluster).location)) {
                        closestCluster = k;
                    }
                }
                //add the data the to closest cluster
                initialClusters.get(closestCluster).addData(dataSet[j]);
            }

            Collections.sort(initialClusters, (s1, s2) -> Double.valueOf(s1.getVariance() - s2.getVariance()).intValue());
            results.put(String.valueOf(i), initialClusters);

        }


        return results;
    }

    public static void main(String[] args) {

        KMeansCluster scratch = new KMeansCluster();
        int[] data = {1, 2, 2, 3, 4, 35, 32, 40, 67, 54, 76, 87, 89, 90};
        Map<String, List<Cluster>> clusters = scratch.kMeans(data);
        System.out.println("--------------------------------------\n");
        clusters.forEach((k,v) -> {
            int sum = 0;
            for (int i = 0; i < v.size(); i++) {
                sum += v.get(i).variance == -1 ? 0 : v.get(i).variance;
            }
            System.out.println(k + "=" + v + "\n" + "variance=" + sum + "\n--------------------------------------");
        });
    }
}