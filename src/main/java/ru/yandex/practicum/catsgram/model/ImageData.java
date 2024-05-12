package ru.yandex.practicum.catsgram.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Class for transmitting data images
 */
@Data
@AllArgsConstructor
public class ImageData {
    private final byte[] data;
    private final String name;
}