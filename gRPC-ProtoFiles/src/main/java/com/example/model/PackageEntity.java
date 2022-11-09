package com.example.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class PackageEntity {
  private @Id @GeneratedValue Long packageId;
  @OneToMany(fetch = FetchType.EAGER)
  private List<TrayEntity> fromTrayEntities;

  public PackageEntity() {
    fromTrayEntities = new ArrayList<>();
  }

  public PackageEntity(List<TrayEntity> fromTrayEntities) {
    this.fromTrayEntities = fromTrayEntities;
  }

  public Long getPackageId() {
    return packageId;
  }

  public void setPackageId(Long packageId) {
    this.packageId = packageId;
  }

  public List<TrayEntity> getTrays() {
    return fromTrayEntities;
  }

  public void setFromTrays(List<TrayEntity> fromTrayEntities) {
    this.fromTrayEntities = fromTrayEntities;
  }

  @Override public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof PackageEntity))
      return false;
    PackageEntity aPackageEntity = (PackageEntity) o;
    return Objects.equals(packageId, aPackageEntity.packageId) && Objects.equals(
        fromTrayEntities, aPackageEntity.fromTrayEntities);
  }

  @Override public int hashCode() {
    return Objects.hash(packageId, fromTrayEntities);
  }

  @Override public String toString() {
    return "Package{" + "packageId=" + packageId + ", fromTrays=" + fromTrayEntities
        + '}';
  }
}
