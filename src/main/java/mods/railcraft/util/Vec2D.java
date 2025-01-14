package mods.railcraft.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;

/**
 * A mutable 2D Vector class that supports a broader range of math operations than Minecraft's
 * immutable Vec3D class.
 *
 * For historical purposes, I borrowed this class from NovaStar, a simple 2D space shooter project I
 * wrote in college.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class Vec2D {

  public static final float DEGREES_45 = (float) (0.25 * Math.PI);
  public static final float DEGREES_90 = (float) (0.5 * Math.PI);
  public static final float DEGREES_135 = (float) (0.75 * Math.PI);
  public static final float DEGREES_180 = (float) (Math.PI);
  public static final float DEGREES_270 = (float) (1.5 * Math.PI);

  private double x;

  private double y;

  public double getX() {
    return this.x;
  }

  public double getY() {
    return this.y;
  }

  public Vec2D() {}

  public Vec2D(final Vector3i p) {
    this(p.getX(), p.getZ());
  }

  public Vec2D(final Vector3d p) {
    this(p.x, p.z);
  }

  public Vec2D(final Entity p) {
    this(p.getX(), p.getZ());
  }

  public Vec2D(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public static Vec2D fromPolar(double angle, float magnitude) {
    Vec2D v = new Vec2D();
    v.setFromPolar(angle, magnitude);
    return v;
  }

  public static Vec2D add(final Vec2D a, final Vec2D b) {
    return new Vec2D(a.getX() + b.getX(), a.getY() + b.getY());
  }

  public static Vec2D subtract(final Vec2D a, final Vec2D b) {
    return new Vec2D(a.getX() - b.getX(), a.getY() - b.getY());
  }

  public static Vec2D unit(final Vec2D a, final Vec2D b) {
    Vec2D unit = subtract(a, b);
    unit.normalize();
    return unit;
  }

  @Override
  public Vec2D clone() {
    return new Vec2D(x, y);
  }

  public void setLocation(double x, double y) {
    this.x = x;
    this.y = y;
  }

  // ==============================================================================
  public void setFromPolar(double angle, float magnitude) {
    float x = (float) Math.cos(angle) * magnitude;
    float y = (float) Math.sin(angle) * magnitude;
    setLocation(x, y);
  }

  public void zero() {
    x = y = 0;
  }

  public void normalize() {
    double mag = magnitude();
    if (mag != 0) {
      setLocation(x / mag, y / mag);
    }
  }

  public Vec2D unitVector() {
    Vec2D v = clone();
    v.normalize();
    return v;
  }

  public double magnitude() {
    return Math.sqrt(x * x + y * y);
  }

  public void setMagnitude(float mag) {
    setFromPolar(angle(), mag);
  }

  public double magnitudeSq() {
    return x * x + y * y;
  }

  public void negate() {
    x = -x;
    y = -y;
  }

  public float angle() {
    return (float) Math.atan2(y, x);
  }

  public void rotate(double theta) {
    double nx = x * (float) Math.cos(theta) - y * (float) Math.sin(theta);
    double ny = x * (float) Math.sin(theta) + y * (float) Math.cos(theta);
    setLocation(nx, ny);
  }

  public void rotate90() {
    double ox = x;
    x = -y;
    y = ox;
  }

  public void rotate270() {
    double ox = x;
    // noinspection SuspiciousNameCombination
    x = y;
    y = -ox;
  }

  public void subtract(final Vec2D p) {
    x -= p.getX();
    y -= p.getY();
  }

  public void subtract(int x, int y) {
    this.x -= x;
    this.y -= y;
  }

  public void subtract(final double x, final double y) {
    this.x -= (int) x;
    this.y -= (int) y;
  }

  public void add(final Vec2D p) {
    x += p.getX();
    y += p.getY();
  }

  public void add(int x, int y) {
    this.x += x;
    this.y += y;
  }

  public void add(final double x, final double y) {
    this.x += (int) x;
    this.y += (int) y;
  }

  // public void transform(AffineTransform a) {
  // setLocation(a.transform(this, null));
  // }
  //
  // public Vec2D makeTransform(AffineTransform a) {
  // return new Vec2D(a.transform(this, null));
  // }

  /**
   * Finds the dot product of two vectors.
   */
  public double dotProduct(final Vec2D v) {
    return x * v.getX() + y * v.getY();
  }

  /**
   * Finds the absolute angle between two vectors using the Law of Cosines:<br>
   * <b>V . W = |V||W|cos(a)</b>
   */
  public double angleBetween(final Vec2D v) {
    double a = dotProduct(v);
    double magnitude = magnitude() * v.magnitude();
    if (magnitude == 0) {
      return 0;
    }
    a /= magnitude;

    // Check bounds, if you don't you WILL get NaNs from acos.
    // This is because of slight errors in the floating point arithmetic.
    if (a > 1) {
      a = 1;
    } else if (a < -1) {
      a = -1;
    }
    return Math.acos(a);
  }

  public double angleTo(Vec2D a) {
    return Math.atan2(a.getY() - y, a.getX() - x);
  }

  public double angleFrom(Vec2D a) {
    return Math.atan2(y - a.getY(), x - a.getX());
  }

  public void scale(float scale) {
    x *= scale;
    y *= scale;
  }

  public void addScale(float scale, Vec2D v) {
    setLocation(x + v.getX() * scale, y + v.getY() * scale);
  }
}
