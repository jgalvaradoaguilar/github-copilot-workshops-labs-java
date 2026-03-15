import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Divide Method Test Suite")
class DivideTest {

    // ✅ SUCCESS SCENARIOS
    @Test
    @DisplayName("Should divide positive numbers correctly")
    void testDividePositiveNumbers() {
        double result = divide(10.0, 2.0);
        assertEquals(5.0, result);
    }

    @Test
    @DisplayName("Should divide resulting in decimal")
    void testDivideWithDecimalResult() {
        double result = divide(10.0, 3.0);
        assertEquals(3.3333333, result, 0.0001);
    }

    // ⚠️ EDGE CASES: ZERO DIVISION
    @Test
    @DisplayName("Should return Infinity when dividing by zero")
    void testDivideByZero() {
        double result = divide(10.0, 0.0);
        assertTrue(Double.isInfinite(result));
    }

    @Test
    @DisplayName("Should return NaN when zero divided by zero")
    void testZeroDividedByZero() {
        double result = divide(0.0, 0.0);
        assertTrue(Double.isNaN(result));
    }

    @Test
    @DisplayName("Should return -Infinity when negative divided by zero")
    void testNegativeDivideByZero() {
        double result = divide(-10.0, 0.0);
        assertTrue(Double.isInfinite(result) && result < 0);
    }

    // ⚠️ EDGE CASES: NEGATIVE NUMBERS
    @Test
    @DisplayName("Should divide negative by positive")
    void testNegativeByPositive() {
        double result = divide(-10.0, 2.0);
        assertEquals(-5.0, result);
    }

    @Test
    @DisplayName("Should divide positive by negative")
    void testPositiveByNegative() {
        double result = divide(10.0, -2.0);
        assertEquals(-5.0, result);
    }

    @Test
    @DisplayName("Should divide negative by negative (positive result)")
    void testNegativeByNegative() {
        double result = divide(-10.0, -2.0);
        assertEquals(5.0, result);
    }

    // ⚠️ EDGE CASES: LARGE NUMBERS
    @Test
    @DisplayName("Should divide large numbers")
    void testDivideLargeNumbers() {
        double result = divide(1_000_000.0, 1_000.0);
        assertEquals(1_000.0, result);
    }

    @Test
    @DisplayName("Should handle very small divisor (large result)")
    void testVerySmallDivisor() {
        double result = divide(1.0, 0.0001);
        assertEquals(10_000.0, result);
    }

    @Test
    @DisplayName("Should handle Double.MAX_VALUE")
    void testMaxDoubleValue() {
        double result = divide(Double.MAX_VALUE, Double.MAX_VALUE);
        assertEquals(1.0, result);
    }

    // ⚠️ EDGE CASES: SPECIAL VALUES
    @Test
    @DisplayName("Should handle zero dividend")
    void testZeroNumerator() {
        double result = divide(0.0, 5.0);
        assertEquals(0.0, result);
    }

    @Test
    @DisplayName("Should preserve sign of zero")
    void testNegativeZeroDividend() {
        double result = divide(-0.0, 5.0);
        assertEquals(-0.0, result);
    }

    @Test
    @DisplayName("Should handle very small numbers")
    void testVerySmallNumbers() {
        double result = divide(0.000001, 0.001);
        assertEquals(0.001, result, 0.0001);
    }

    // Helper method (for testing purposes)
    double divide(double a, double b) {
        return a / b;
    }
}
