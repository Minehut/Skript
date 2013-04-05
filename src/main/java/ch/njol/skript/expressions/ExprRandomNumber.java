/*
 *   This file is part of Skript.
 *
 *  Skript is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Skript is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 * Copyright 2011-2013 Peter Güttinger
 * 
 */

package ch.njol.skript.expressions;

import java.util.Random;

import org.bukkit.event.Event;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;

/**
 * @author Peter Güttinger
 */
@SuppressWarnings("serial")
@Name("Random Number")
@Description("A random number or integer between two given numbers. Use 'number' if you want any number with decimal parts, or use use 'integer' if you only want whole numbers.")
@Examples({"set the player's health to a random number between 5 and 10",
		"send \"You rolled a %random integer from 1 to 6%!\" to the player"})
@Since("1.4")
public class ExprRandomNumber extends SimpleExpression<Number> {
	static {
		Skript.registerExpression(ExprRandomNumber.class, Number.class, ExpressionType.NORMAL,
				"[a] random (1¦integer|2¦number) (from|between) %number% (to|and) %number%");
	}
	
	private Expression<? extends Number> lower, upper;
	
	private final Random rand = new Random();
	
	private boolean integer;
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final ParseResult parser) {
		lower = (Expression<Number>) exprs[0];
		upper = (Expression<Number>) exprs[1];
		integer = parser.mark == 1;
		return true;
	}
	
	@Override
	protected Number[] get(final Event e) {
		final Number l = lower.getSingle(e);
		final Number u = upper.getSingle(e);
		
		if (u == null || l == null)
			return null;
		
		if (integer) {
			return new Integer[] {(int) (Math.ceil(l.doubleValue()) + rand.nextInt((int) (Math.floor(u.doubleValue()) - Math.ceil(l.doubleValue()) + 1)))};
		} else {
			return new Double[] {l.doubleValue() + rand.nextDouble() * (u.doubleValue() - l.doubleValue())};
		}
	}
	
	@Override
	public Class<? extends Number> getReturnType() {
		return integer ? Integer.class : Double.class;
	}
	
	@Override
	public String toString(final Event e, final boolean debug) {
		return "a random number between " + lower + " and " + upper;
	}
	
	@Override
	public boolean isSingle() {
		return true;
	}
	
	@Override
	public boolean getAnd() {
		return true;
	}
	
}