import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class Generator {
    public int mTextSize;
    public long mRangeSize;
    private static long mEnd;
    private static int mBase;
    public static Map<Long, Long> mRangeMap = new ConcurrentHashMap<Long, Long>();
    public Generator(int base, int maxChars, int rangeSize) {
        mEnd = (long) Math.pow(base, maxChars);
        mBase = base;
        mRangeSize = rangeSize;
        mTextSize = maxChars;
    }
    public long next_range() {
        if(mTextSize == 0 && mRangeMap.isEmpty()) return -1;

        if(mEnd == 0) {
            if(!mRangeMap.isEmpty()) {
                java.util.List<Long> keysArray = new java.util.ArrayList<Long>(mRangeMap.keySet());
                return keysArray.get(0);
            }
            mTextSize = mTextSize - 1;
            mEnd = (long) Math.pow(mBase, mTextSize);
        }
        long rngEnd = mEnd;
        mRangeMap.put(rngEnd, rngEnd);
        mEnd = Math.max(0, mEnd - mRangeSize);
        return rngEnd;
    }
}