package com.example.todo_app.common.generators;

import com.github.f4b6a3.uuid.UuidCreator;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;

import java.util.EnumSet;

/**
 * Custom Hibernate ID generator for UUID version 7.
 * UUID v7 is time-ordered and provides better database performance
 * compared to random UUIDs (v4) due to better locality.
 */
public class UuidV7Generator implements BeforeExecutionGenerator {

    @Override
    public Object generate(SharedSessionContractImplementor session, Object owner, Object currentValue, EventType eventType) {
        return UuidCreator.getTimeOrderedEpoch().toString();
    }
    @Override
    public EnumSet<EventType> getEventTypes() {
        return EnumSet.of(EventType.INSERT);
    }
}