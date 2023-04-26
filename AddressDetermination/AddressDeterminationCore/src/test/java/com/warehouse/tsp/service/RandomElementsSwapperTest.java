package com.warehouse.tsp.service;

import com.warehouse.tsp.domain.service.RandomElementsSwapper;
import com.warehouse.tsp.domain.service.RandomElementsSwapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RandomElementsSwapperTest {

    private final RandomElementsSwapper randomElementsSwapper = new RandomElementsSwapperImpl();

    @Test
    void shouldSwapRandomElementsy() {
        // given
        final List<Integer> elements = Arrays.asList(1, 2, 3, 4, 5);

        // when
        final List<Integer> swappedElements = randomElementsSwapper.swapRandomElements(elements);

        // then
        assertEquals(elements.size(), swappedElements.size());
    }

    @Test
    void shouldNotSwapRandomElementsWhenArrayListIsEmpty() {
        // given empty array list
        final List<Integer> emptyRoute = new ArrayList<>();

        // when
        final Executable executable = () -> randomElementsSwapper.swapRandomElements(emptyRoute);

        // then
        final IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class, executable);
        assertThat(exception.getMessage()).isEqualTo("Index 0 out of bounds for length 0");
    }

}
