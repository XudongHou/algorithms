package com.hxd.context.simulation;

import java.awt.Color;

import com.hxd.introcs.stdlib.StdDraw;
import com.hxd.introcs.stdlib.StdRandom;

/**
 * 事件驱动模拟问题:维护一个复杂的真实世界模型并根据时间控制模型中发生的变化 ,
 * 理想模型:原子和分子在含有以下性质的容器中的运动:
 * 		运动的粒子与墙以及相互之间的碰撞都是弹性的;
 * 		每个粒子都是一个已知位置,速速,质量和直径的球体;
 * 		不存在其他外力;
 * 对于N个能够互相碰撞的粒子系统,基于事件的模拟在初始化时最多需要N^2次优先队列操作,
 * 在碰撞时最多需要N次优先队列操作(且对于每个无效的事件都需要一次额外的操作)
 * @author houxu_000 20170408
 *
 */
public class Particle {
	
	/**
	 * 时间驱动模拟,基于使用固定长度的dt.在每次更新时,检查所有粒子对,判定是否可能相遇,然后还原碰撞,更新
     * 两个粒子的速度以反映碰撞结果,如果dt是以秒,模拟N个粒子的系统一秒钟的运动所需的与N^2/dt成正比.成本
     * 太高
     * 
     * 事件驱动模拟,仅关注碰撞发生的时间点,重点关注下一次的碰撞(因为在此之前有速度计算得到的所有粒子的位
     * 置都是有效的).使用一个优先队列来记录所有事件.事件是未来的某个时间的一次潜在碰撞,当从优先队列中删除
     * 优先级最低的元素时,就会得到下一次潜在的碰撞.
	 */
	
	//未发生碰撞返回值
    private static final double INFINITY = Double.POSITIVE_INFINITY;

    private double rx, ry;        // position
    private double vx, vy;        // velocity
    private int count;            // number of collisions so far
    private final double radius;  // radius
    private final double mass;    // mass
    private final Color color;    // color


    /**
     * Initializes a particle with the specified position, velocity, radius, mass, and color.
     *	用给定的位置 速度 半径  质量 颜色创建一个粒子
     * @param  rx <em>x</em>-coordinate of position
     * @param  ry <em>y</em>-coordinate of position
     * @param  vx <em>x</em>-coordinate of velocity
     * @param  vy <em>y</em>-coordinate of velocity
     * @param  radius the radius
     * @param  mass the mass
     * @param  color the color
     */
    public Particle(
    		double rx, double ry, 
    		double vx, double vy, 
    		double radius, double mass, 
    		Color color) {
        this.vx = vx;
        this.vy = vy;
        this.rx = rx;
        this.ry = ry;
        this.radius = radius;
        this.mass   = mass;
        this.color  = color;
    }
         
    /**
     * Initializes a particle with a random position and velocity.
     * The position is uniform in the unit box; the velocity in
     * either direciton is chosen uniformly at random.
     */
    public Particle() {
        rx     = StdRandom.uniform(0.0, 1.0);
        ry     = StdRandom.uniform(0.0, 1.0);
        vx     = StdRandom.uniform(-0.005, 0.005);
        vy     = StdRandom.uniform(-0.005, 0.005);
        radius = 0.01;
        mass   = 0.5;
        color  = Color.BLACK;
    }

    /**
     * 画出粒子
	 * Draws this particle to standard draw.
	 */
	public void draw() {
	    StdDraw.setPenColor(color);
	    StdDraw.filledCircle(rx, ry, radius);
	}

	/**
	 * 根据时间的流逝改变粒子的位置
     * Moves this particle in a straight line (based on its velocity)
     * for the specified amount of time.
     *
     * @param  dt the amount of time
     */
    public void move(double dt) {
        rx += vx * dt;
        ry += vy * dt;
    }

    /**
     * 该粒子所参与的碰撞总数
     * Returns the number of collisions involving this particle with
     * vertical walls, horizontal walls, or other particles.
     * This is equal to the number of calls to {@link #bounceOff},
     * {@link #bounceOffVerticalWall}, and
     * {@link #bounceOffHorizontalWall}.
     *
     * @return the number of collisions involving this particle with
     *         vertical walls, horizontal walls, or other particles
     */
    public int count() {
        return count;
    }

    /**
     * 距离该粒子和粒子that碰撞所需的时间 
     * Returns the amount of time for this particle to collide with the specified
     * particle, assuming no interening collisions.
     *
     * @param  that the other particle
     * @return the amount of time for this particle to collide with the specified
     *         particle, assuming no interening collisions; 
     *         {@code Double.POSITIVE_INFINITY} if the particles will not collide
     */
    public double timeToHit(Particle that) {
        if (this == that) return INFINITY;
        double dx  = that.rx - this.rx;
        double dy  = that.ry - this.ry;
        double dvx = that.vx - this.vx;
        double dvy = that.vy - this.vy;
        double dvdr = dx*dvx + dy*dvy;		//做功比较
        if (dvdr > 0) return INFINITY;
        double dvdv = dvx*dvx + dvy*dvy;
        double drdr = dx*dx + dy*dy;
        double sigma = this.radius + that.radius;
        double d = (dvdr*dvdr) - dvdv * (drdr - sigma*sigma);
        // if (drdr < sigma*sigma) StdOut.println("overlapping particles");
        if (d < 0) return INFINITY;
        return -(dvdr + Math.sqrt(d)) / dvdv;
    }

    /**
     * 距离该粒子和水平墙体碰撞所需的时间
     * Returns the amount of time for this particle to collide with a vertical
     * wall, assuming no interening collisions.
     *
     * @return the amount of time for this particle to collide with a vertical wall,
     *         assuming no interening collisions; 
     *         {@code Double.POSITIVE_INFINITY} if the particle will not collide
     *         with a vertical wall
     */
    public double timeToHitVerticalWall() {
        if      (vx > 0) return (1.0 - rx - radius) / vx;
        else if (vx < 0) return (radius - rx) / vx;  
        else             return INFINITY;
    }

    /**
     * 距离该粒子和垂直墙体碰撞所需的时间
     * Returns the amount of time for this particle to collide with a horizontal
     * wall, assuming no interening collisions.
     *
     * @return the amount of time for this particle to collide with a horizontal wall,
     *         assuming no interening collisions; 
     *         {@code Double.POSITIVE_INFINITY} if the particle will not collide
     *         with a horizontal wall
     */
    public double timeToHitHorizontalWall() {
        if      (vy > 0) return (1.0 - ry - radius) / vy;
        else if (vy < 0) return (radius - ry) / vy;
        else             return INFINITY;
    }

    /**
     * 碰撞后粒子的速度
     * Updates the velocities of this particle and the specified particle according
     * to the laws of elastic collision. Assumes that the particles are colliding
     * at this instant.
     *
     * @param  that the other particle
     */
    public void bounceOff(Particle that) {
        double dx  = that.rx - this.rx;
        double dy  = that.ry - this.ry;
        double dvx = that.vx - this.vx;
        double dvy = that.vy - this.vy;
        double dvdr = dx*dvx + dy*dvy;             // dv dot dr
        double dist = this.radius + that.radius;   // distance between particle centers at collison

        // magnitude of normal force
        double magnitude = 2 * this.mass * that.mass * dvdr / ((this.mass + that.mass) * dist);

        // normal force, and in x and y directions
        double fx = magnitude * dx / dist;
        double fy = magnitude * dy / dist;

        // update velocities according to normal force
        this.vx += fx / this.mass;
        this.vy += fy / this.mass;
        that.vx -= fx / that.mass;
        that.vy -= fy / that.mass;

        // update collision counts
        this.count++;
        that.count++;
    }

    /**
     * 碰撞垂直墙体后该粒子的速度
     * Updates the velocity of this particle upon collision with a vertical
     * wall (by reflecting the velocity in the <em>x</em>-direction).
     * Assumes that the particle is colliding with a vertical wall at this instant.
     */
    public void bounceOffVerticalWall() {
        vx = -vx;
        count++;
    }

    /**
     * 碰撞水平墙体后该粒子的速度
     * Updates the velocity of this particle upon collision with a horizontal
     * wall (by reflecting the velocity in the <em>y</em>-direction).
     * Assumes that the particle is colliding with a horizontal wall at this instant.
     */
    public void bounceOffHorizontalWall() {
        vy = -vy;
        count++;
    }

    /**
     * Returns the kinetic energy of this particle.
     * The kinetic energy is given by the formula 1/2 <em>m</em> <em>v</em><sup>2</sup>,
     * where <em>m</em> is the mass of this particle and <em>v</em> is its velocity.
     *
     * @return the kinetic energy of this particle
     */
    public double kineticEnergy() {
        return 0.5 * mass * (vx*vx + vy*vy);
    }
}