package lab2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Lab2 {
    private static final Map<String, Validator<?>> vals = new HashMap<>();
    private static final Map<String, String> types = new HashMap<>();

    private static String show(ValidationResult r) {
        return r.isValid() ? "Valid" : "Invalid: " + r.getErrors();
    }

    private static boolean missing(String name) {
        if (!vals.containsKey(name)) {
            System.out.println("No validator " + name);
            return true;
        }
        return false;
    }

    private static Validator<String> strAtom(String tok) {
        if (tok.equals("NOTNULL")) return Validator.<String>notNull();
        if (tok.startsWith("MINLEN:")) return Validator.minLength(Integer.parseInt(tok.substring(7)));
        if (tok.equals("HASAT")) {
            return s -> s.contains("@") ? ValidationResult.ok() : ValidationResult.error("must contain @");
        }
        throw new IllegalStateException("unknown atom " + tok);
    }

    private static Validator<Integer> intAtom(String tok) {
        if (tok.equals("NOTNULL")) return Validator.<Integer>notNull();
        if (tok.startsWith("RANGE:")) {
            String[] r = tok.split(":");
            return Validator.range(Integer.parseInt(r[1]), Integer.parseInt(r[2]));
        }
        throw new IllegalStateException("unknown atom " + tok);
    }

    private static String strValue(String t) {
        return t.equals("NULL") ? null : t;
    }

    private static Integer intValue(String t) {
        return t.equals("NULL") ? null : Integer.valueOf(t);
    }

    private static List<String> errorList(String t) {
        List<String> list = new ArrayList<>();
        if (!t.equals("-")) list.addAll(Arrays.asList(t.split(",")));
        return list;
    }

    @SuppressWarnings("unchecked")
    private static void run(String[] p) {
        switch (p[0]) {
            //   VAL <name> <STR|INT> <expr>   a chain of atoms joined with '>' (= .and(next))
            //        STR atoms: NOTNULL, MINLEN:<n>, HASAT (a lambda: "must contain @"), OBJNOTNULL (Validator<Object>.notNull())
            //        INT atoms: NOTNULL, RANGE:<min>:<max>
            case "VAL": {
                String[] toks = p[3].split(">");
                if (p[2].equals("STR")) {
                    Validator<String> v = null;
                    for (String tok : toks) {
                        if (tok.equals("OBJNOTNULL")) {
                            v = v.and(Validator.<Object>notNull());          // Validator<Object> where Validator<? super String> is expected
                        } else {
                            Validator<String> next = strAtom(tok);
                            v = (v == null) ? next : v.and(next);
                        }
                    }
                    vals.put(p[1], v);
                } else {
                    Validator<Integer> v = null;
                    for (String tok : toks) {
                        Validator<Integer> next = intAtom(tok);
                        v = (v == null) ? next : v.and(next);
                    }
                    vals.put(p[1], v);
                }
                types.put(p[1], p[2]);
                System.out.println("Defined " + p[1] + " (" + p[2] + ")");
                break;
            }
            case "CHECK": {
                if (missing(p[1])) break;
                ValidationResult r = types.get(p[1]).equals("STR")
                        ? ((Validator<String>) vals.get(p[1])).validate(strValue(p[2]))
                        : ((Validator<Integer>) vals.get(p[1])).validate(intValue(p[2]));
                System.out.println(show(r));
                break;
            }
            case "AND_NULL":
                if (missing(p[1])) break;
                ((Validator<String>) vals.get(p[1])).and(null);
                System.out.println("No exception");
                break;
            case "MODIFY": {
                if (missing(p[1])) break;
                ValidationResult r = types.get(p[1]).equals("STR")
                        ? ((Validator<String>) vals.get(p[1])).validate(strValue(p[2]))
                        : ((Validator<Integer>) vals.get(p[1])).validate(intValue(p[2]));
                r.getErrors().add("x");
                System.out.println("Modified");
                break;
            }
            case "RESULT_OK":
                System.out.println("ok: " + show(ValidationResult.ok()));
                break;
            case "RESULT_OK_MODIFY":
                ValidationResult.ok().getErrors().add("x");
                System.out.println("Modified");
                break;
            case "RESULT_ERROR":
                System.out.println("error: " + show(ValidationResult.error(p[1].equals("NULL") ? null : p[1].equals("EMPTY") ? "" : p[1])));
                break;
            case "RESULT_ERROR_MODIFY":
                ValidationResult.error(p[1]).getErrors().add("x");
                System.out.println("Modified");
                break;
            case "RESULT_OF":
                System.out.println("of: " + show(ValidationResult.of(errorList(p[1]))));
                break;
            case "RESULT_OF_COPY": {
                List<String> src = errorList(p[1]);
                ValidationResult r = ValidationResult.of(src);
                src.add("late");
                if (src.size() > 1) src.set(0, "changed");
                System.out.println("after change: " + show(r));
                break;
            }
            case "RESULT_OF_MODIFY":
                ValidationResult.of(errorList(p[1])).getErrors().add("x");
                System.out.println("Modified");
                break;
            case "RESULT_OF_NULL":
                ValidationResult.of(null);
                System.out.println("No exception");
                break;
            case "USER":
                System.out.println(show(UserValidation.validate(new User(strValue(p[1]), intValue(p[2]), strValue(p[3])))));
                break;
            case "USER_NULL":
                UserValidation.validate(null);
                System.out.println("No exception");
                break;
            default:
                System.out.println("Unknown command");
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String[] p = line.split("\\s+");
            if (p[0].equals("END")) break;
            try {
                run(p);
            } catch (IllegalArgumentException e) {
                System.out.println("IllegalArgumentException");
            } catch (UnsupportedOperationException e) {
                System.out.println("UnsupportedOperationException");
            }
        }
    }
}
