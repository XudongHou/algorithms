package com.hxd.context.simulation;

import java.awt.Color;
import java.io.File;

import com.hxd.introcs.stdlib.In;
import com.hxd.introcs.stdlib.StdDraw;
import com.hxd.sort.priorityQueue.MinPQ;

/**
 * 
 * @author houxu_000 20170408
 *
 */
public class CollisionSystem {
	private final static double HZ = 0.5; // number of redraw events per clock
											// tick

	private MinPQ<Event> pq; // the priority queue
	private double t = 0.0; // simulation clock time
	private Particle[] particles; // the array of particles

	/**
	 * Initializes a system with the specified collection of particles. The
	 * individual particles will be mutated during the simulation.
	 *
	 * @param particles
	 *            the array of particles
	 */
	public CollisionSystem(Particle[] particles) {
		this.particles = particles.clone(); // defensive copy
	}

	// updates priority queue with all new events for particle a
	private void predict(Particle a, double limit) {
		if (a == null)
			return;

		// 重绘事件
		// particle-particle collisions
		for (int i = 0; i < particles.length; i++) {
			//将与particles[i]发生碰撞的事件插入pq中
			double dt = a.timeToHit(particles[i]);
			if (t + dt <= limit)
				pq.insert(new Event(t + dt, a, particles[i]));
		}

		// particle-wall collisions
		double dtX = a.timeToHitVerticalWall();
		double dtY = a.timeToHitHorizontalWall();
		if (t + dtX <= limit)
			pq.insert(new Event(t + dtX, a, null));
		if (t + dtY <= limit)
			pq.insert(new Event(t + dtY, null, a));
	}

	// redraw all particles
	private void redraw(double limit) {
//		重新画出所有粒子
		StdDraw.clear();
		for (int i = 0; i < particles.length; i++) {
			particles[i].draw();
		}
		StdDraw.show();
		StdDraw.pause(20);
		if (t < limit) {
			pq.insert(new Event(t + 1.0 / HZ, null, null));
		}
	}

	/**
	 * 模拟过程
	 * 使用一个limit来指定有效的时间段,忽略时间晚于limit发生的所有时间
	 * Simulates the system of particles for the specified amount of time.
	 *
	 * @param limit
	 *            the amount of time
	 */
	public void simulate(double limit) {

		// initialize PQ with collision events and redraw event
		pq = new MinPQ<Event>();
		for (int i = 0; i < particles.length; i++) {
			predict(particles[i], limit);
		}
		pq.insert(new Event(0, null, null)); // redraw event

		// the main event-driven simulation loop
		while (!pq.isEmpty()) {

			// get impending event, discard if invalidated
			Event e = pq.delMin();
			//过滤
			if (!e.isValid())
				continue;
			Particle a = e.a;
			Particle b = e.b;

			// physical collision, so update positions, and then simulation
			// clock
			for (int i = 0; i < particles.length; i++)
				particles[i].move(e.time - t);
			t = e.time;

			//重绘事件
			// process event
			if (a != null && b != null)
				a.bounceOff(b); // particle-particle collision
			else if (a != null && b == null)
				a.bounceOffVerticalWall(); // particle-wall collision
			else if (a == null && b != null)
				b.bounceOffHorizontalWall(); // particle-wall collision
			else if (a == null && b == null)
				redraw(limit); // redraw event

			// update the priority queue with new collisions involving a or b
			predict(a, limit);
			predict(b, limit);
		}
	}

	/***************************************************************************
	 * An event during a particle collision simulation. Each event contains the
	 * time at which it will occur (assuming no supervening actions) and the
	 * particles a and b involved.
	 *	需要三个公式:能量守恒,动能守恒,碰撞时,相互作用力和碰撞点的切面垂直 
	 * - a and b both null: redraw event - a null, b not null: collision with
	 * vertical wall - a not null, b null: collision with horizontal wall - a
	 * and b both not null: binary collision between a and b
	 *
	 ***************************************************************************/
	private static class Event implements Comparable<Event> {
		
		private final double time; // time that event is scheduled to occur
		private final Particle a, b; // particles involved in event, possibly
										// null
		private final int countA, countB; // collision counts at event creation

		// create a new event to occur at time t involving a and b
		public Event(double t, Particle a, Particle b) {
			this.time = t;
			this.a = a;
			this.b = b;
			if (a != null)
				countA = a.count();
			else
				countA = -1;
			if (b != null)
				countB = b.count();
			else
				countB = -1;
		}

		// compare times when two events will occur
		public int compareTo(Event that) {
			return Double.compare(this.time, that.time);
		}

		/**
		 *  记录事件创建时每个粒子所参与的碰撞事件数量,如果这个事件进入优先队列和离开优先队列的这段时间
		 *  任何计数器发生了变化,这个事件就失效了,那就可以忽略
		 */
		// has any collision occurred between when event was created and now?
		public boolean isValid() {
			if (a != null && a.count() != countA)
				return false;
			if (b != null && b.count() != countB)
				return false;
			return true;
		}

	}

	/**
	 * Unit tests the {@code CollisionSystem} data type. Reads in the particle
	 * collision system from a standard input (or generates {@code N} random
	 * particles if a command-line integer is specified); simulates the system.
	 *
	 * @param args
	 *            the command-line arguments
	 */
	public static void main(String[] args) {

		StdDraw.setCanvasSize(800, 800);

		// remove the border
		 StdDraw.setXscale(1.0/22.0, 21.0/22.0);
		 StdDraw.setYscale(1.0/22.0, 21.0/22.0);

		// enable double buffering
		StdDraw.enableDoubleBuffering();

		// the array of particles
		Particle[] particles;

		// create n random particles
		if (args.length == 1) {
			int n = Integer.parseInt(args[0]);
			particles = new Particle[n];
			for (int i = 0; i < n; i++)
				particles[i] = new Particle();
		}

		// or read from standard input
		else {
			File[] files = new File[] { 
//					new File(".\\algs4-data\\diffusion.txt"),
					new File(".\\algs4-data\\brownian.txt"),
					};
			for (File file : files) {
				In in = new In(file);
				int n = in.readInt();
				particles = new Particle[n];
				for (int i = 0; i < n; i++) {
					double rx = in.readDouble();
					double ry = in.readDouble();
					double vx = in.readDouble();
					double vy = in.readDouble();
					double radius = in.readDouble();
					double mass = in.readDouble();
					int r = in.readInt();
					int g = in.readInt();
					int b = in.readInt();
					Color color = new Color(r, g, b);
					particles[i] = new Particle(rx, ry, vx, vy, radius, mass, color);
				}
				// create collision system and simulate
				CollisionSystem system = new CollisionSystem(particles);
				system.simulate(10000);
			}
		}

		
	}
}
