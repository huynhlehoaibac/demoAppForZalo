package com.pa.demo.common.mapper;

/**
 * A mapper interface, it converts an entity to a DTO and vice-versa
 *
 * @param <E> Entity
 * @param <D> DTO
 * @author Z.DRISSI
 */
public interface Mapper<E, D> {

  /**
   * Converts an entity to a DTO
   *
   * @param source an entity object
   * @return DTO
   */
  E toEntity(D source);

  /**
   * Converts a DTO into an entity
   *
   * @param source a dto object
   * @return entity
   */
  D toDTO(E source);
}
