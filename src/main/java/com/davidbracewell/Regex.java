package com.davidbracewell;

import com.davidbracewell.string.StringUtils;
import lombok.NonNull;
import lombok.Value;

import java.io.Serializable;
import java.util.regex.Pattern;

import static com.davidbracewell.Re.re;

/**
 * <p>Fluent style regular expression builder.</p>
 *
 * @author David B. Bracewell
 */
@Value
public final class Regex implements Serializable {
   private static final long serialVersionUID = 1L;
   private final String pattern;

   /**
    * Instantiates a new Regex.
    *
    * @param pattern the pattern
    * @param quote   the quote
    */
   Regex(String pattern, boolean quote) {
      if (pattern != null) {
         if (quote) {
            this.pattern = Pattern.quote(pattern);
         } else {
            this.pattern = pattern;
         }
      } else {
         this.pattern = StringUtils.EMPTY;
      }

   }

   /**
    * To chars string.
    *
    * @param p the p
    * @return the string
    */
   static String toChars(String p) {
      if (p.length() >= 3 && p.charAt(0) == '[' && p.charAt(p.length() - 1) == ']') {
         return p.substring(1, p.length() - 1);
      }
      return p;
   }

   /**
    * Concatenates the given regex with this one.
    *
    * @param regex the regex to concatenate with this one
    * @return the regex
    */
   public Regex then(Regex regex) {
      if (regex == null) {
         return this;
      }
      return re(this.pattern + regex.pattern);
   }

   /**
    * Appends a dollar sign to the end of the regex signifying that the pattern must match to the end of line.
    *
    * @return the regex
    */
   public Regex endLine() {
      return re(this.pattern + "$");
   }


   /**
    * Ors together this regex with the supplied other regular expressions
    *
    * @param others the other regular expressions to be ored with this one
    * @return the regex
    */
   public Regex or(@NonNull Regex... others) {
      if (others == null) {
         return this;
      }
      return Re.or(this, others);
   }

   /**
    * Ands together this regex with the supplied regular expression
    *
    * @param other the other regular expression to be anded to this one
    * @return the regex
    */
   public Regex and(@NonNull Regex other) {
      if (other.pattern.length() > 0) {
         return re(this.pattern + "&&" + other.pattern);
      }
      return this;
   }

   /**
    * Converts the regex into a group. If the supplied name is not null or blank, the group will be named.
    *
    * @param name the name of the group
    * @return the regex
    */
   public Regex group(String name) {
      return re("(" + (StringUtils.isNotNullOrBlank(name) ? "?<" + name + ">" : StringUtils.EMPTY) + pattern + ")");
   }

   /**
    * Converts the regex into a non-matching group
    *
    * @return the regex
    */
   public Regex nmGroup() {
      return re("(?:" + pattern + ")");
   }

   /**
    * Converts the regex into a group
    *
    * @return the regex
    */
   public Regex group() {
      return re("(" + this.pattern + ")");
   }

   /**
    * Negates the regex
    *
    * @return the negated regex
    */
   public Regex not() {
      if (this.pattern.length() > 0) {
         if (this.pattern.charAt(0) == '[' && this.pattern.length() > 1) {
            return re("[^" + this.pattern.substring(1));
         }
         return re("^" + this.pattern);
      }
      return this;
   }

   /**
    * Specifies the number of times for this regex to repeat.
    *
    * @param n the number of times the pattern should repeat
    * @return the regex
    */
   public Regex nTimes(int n) {
      return re(this.pattern + "{" + Integer.toString(n) + "}");
   }

   /**
    * Specifies the minimum and maximum times for this regex to repeat.
    *
    * @param min the minimum times the pattern should repeat
    * @param max the maximum times the pattern should repeat
    * @return the regex
    */
   public Regex range(int min, int max) {
      return re(this.pattern + "{" + Integer.toString(min) + "," + Integer.toString(max) + "}");
   }

   /**
    * Appends a plus sign to the end of the regex. Typically this is used to designate a match of one or more.
    *
    * @return the regex
    */
   public Regex plus() {
      return re(this.pattern + "+");
   }

   /**
    * Appends a asterisks to the end of the regex. Typically this is used to designate a match of zero or more.
    *
    * @return the regex
    */
   public Regex star() {
      return re(this.pattern + "*");
   }


   /**
    * Appends a question mark to the end of the regex. Typically this is used to designate a match of zero or one.
    *
    * @return the regex
    */
   public Regex question() {
      return re(this.pattern + "?");
   }


   /**
    * Converts the regex object to a Java pattern with the specified flags.
    *
    * @return the Java regular expression pattern
    */
   public Pattern toPattern() {
      return Pattern.compile(pattern);
   }


   /**
    * Converts the regex object to a Java pattern with the specified flags.
    *
    * @param flags the flags for the pattern creation see {@link Pattern}
    * @return the Java regular expression pattern
    */
   public Pattern toPattern(int flags) {
      return Pattern.compile(pattern, flags);
   }

   @Override
   public String toString() {
      return pattern;
   }

   /**
    * Converts this regex to match an entire line.
    *
    * @return the regex that matches entire lines.
    */
   public Regex matchLine() {
      return re("^" + this.pattern + "$");
   }

   /**
    * Converts this regex into a character class.
    *
    * @return the character class regex
    */
   public Regex chars() {
      return Re.chars(this);
   }


}// END OF Regex

