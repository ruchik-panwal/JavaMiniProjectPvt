// File: src/com/bloodBank/model/BloodStatus.java
package com.bloodBank.model;

/**
 * Represents the fixed set of states for a blood unit.
 * This is public so all packages (like 'view') can use it.
 */
public enum BloodStatus {
    IN_STOCK,
    ISSUED,
    EXPIRED
}