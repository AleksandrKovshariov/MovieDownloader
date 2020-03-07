package downloader.generator;

import java.io.IOException;
import java.net.URL;
import java.util.function.Function;
import java.util.function.LongUnaryOperator;

public class IncrementingUrlGenerator implements GeneratorStrategy{
    private final String baseUrl;
    private final LongUnaryOperator operator;
    private long currentIndex;

    public IncrementingUrlGenerator(String baseUrl, int initialValue, LongUnaryOperator operator) {
        this.baseUrl = baseUrl;
        this.currentIndex = initialValue;
        this.operator = operator;
    }

    public IncrementingUrlGenerator(String baseUrl, int initialValue) {
        this(baseUrl, initialValue, x -> x + 1);
    }

    public IncrementingUrlGenerator(String baseUrl) {
        this(baseUrl, 0);
    }

    @Override
    public URL next() throws IOException {
        URL url = new URL(String.format(baseUrl, currentIndex));
        currentIndex = operator.applyAsLong(currentIndex);
        return url;
    }
}
