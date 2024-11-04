import java.util.HashSet;

class Main {

    public static int exists(Entry[] arr, int i) {
        for (int j = 0; j < arr.length; j++) {
            if (arr[j] != null && arr[j].val == i) {
                return j;
            }
        }
        return -1;
    }

    public static double fifo(int[] accesses, int size) {
        Entry[] cache = new Entry[size];
        int hits = 0;
        int time = 0;
        for (int i : accesses) {
            int exists = exists(cache, i);
            if (exists != -1) {
                hits++;
            } else {
                timeReplace(cache, time, i);
            }
            time++;
        }
        for (Entry e : cache) {
            System.out.print(e.val + " ");
        }
        System.out.println();
        return (double) hits / accesses.length;
    }

    public static double rand(int[] accesses, int size) {
        Entry[] cache = new Entry[size];
        int hits = 0;
        int time = 0;
        for (int i : accesses) {
            int exists = exists(cache, i);
            if (exists != -1) {
                hits++;
                cache[exists].time = time;
            } else {
                int j = 0;
                while (j < cache.length && cache[j] != null) {
                    j++;
                }
                if (j < cache.length) {
                    cache[j] = new Entry(i, time);
                } else {
                    cache[(int) (Math.random() * size)] = new Entry(i, time);
                }
            }
            time++;
        }

        return (double) hits / accesses.length;
    }

    public static double lru(int[] accesses, int size) {
        Entry[] cache = new Entry[size];
        int hits = 0;
        int time = 0;
        for (int i : accesses) {
            int exists = exists(cache, i);
            if (exists != -1) {
                hits++;
                cache[exists].time = time;
            } else {
                timeReplace(cache, time, i);
            }
            time++;
        }
        for (Entry e : cache) {
            System.out.print(e.val + " ");
        }
        System.out.println();
        return (double) hits / accesses.length;
    }

    private static void timeReplace(Entry[] cache, int time, int i) {
        int index = -1;
        int min = Integer.MAX_VALUE;
        int j = 0;
        while (j < cache.length && cache[j] != null) {
            j++;
        }
        if (j < cache.length) {
            cache[j] = new Entry(i, time);
        } else {
            for (int k = 0; k < cache.length; k++) {
                if (cache[k] != null && cache[k].time < min) {
                    index = k;
                    min = cache[k].val;
                }
            }
            cache[Math.max(index, 0)] = new Entry(i, time);
        }
    }

    public static double optimal(int[] accesses, int size) {
        Entry[] cache = new Entry[size];
        int hits = 0;
        int time = 0;
        for (int k = 0; k < accesses.length; k++) {
            int i = accesses[k];
            int exists = exists(cache, i);
            if (exists != -1) {
                hits++;
                cache[exists].time = time;
//                System.out.println("Hit " + i);
            } else {
                int index = -1;
                int min = Integer.MAX_VALUE;
                int j = 0;
                while (j < cache.length && cache[j] != null) {
                    j++;
                }
                if (j < cache.length) {
//                    System.out.println("Compulsory miss " + i);
                    cache[j] = new Entry(i, time);
                } else {
                    HashSet<Integer> set = new HashSet<>();
                    for (Entry e : cache) {
                        set.add(e.val);
                    }
                    int max = -1;
                    int t = k;
                    while (t < accesses.length) {
                        if (set.remove(accesses[t])) {
                            max = indexOf(accesses[t], cache);
                        }
                        t++;
                    }
                    if (!set.isEmpty()) {
                        Integer next = set.iterator().next();
//                        System.out.println("Evict dupe " + cache[indexOf(next, cache)].val);
                        cache[indexOf(next, cache)] = new Entry(i, time);
                    } else {
//                        System.out.println("Evict " + cache[max].val);
                        cache[Math.max(max, 0)] = new Entry(i, time);
                    }
                }
            }
            time++;
        }
        for (Entry e : cache) {
            System.out.print(e.val + " ");
        }
        System.out.println();
        return (double) hits / accesses.length;
    }

    public static int indexOf(int n, Entry[] arr) {
        int i = 0;
        while (i < arr.length && n != arr[i].val) {
            i++;
        }
        return i;
    }


    public static void main(String[] args) {
        int[] accesses = new int[]{1, 2, 3, 4, 5, 1, 4, 1, 2, 3, 4, 5};
        System.out.println("Belady's MIN: " + optimal(accesses, 4));
        System.out.println("FIFO " + fifo(accesses, 4));
        double avg = 0;
        for (int i = 0; i < 10000; i++) {
            avg += rand(accesses, 4) / 10000;
        }
        System.out.println("RAND: " + avg);
        System.out.println("LRU: " + lru(accesses, 4));
    }

    static class Entry {
        public int val;
        public int time;

        public Entry(int val, int time) {
            this.val = val;
            this.time = time;
        }
    }
}