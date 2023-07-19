package corporation.model.bookkeeping.convenience;

import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import corporation.model.bookkeeping.Book;
import corporation.model.bookkeeping.Transaction;

public class ScriptTransactionFactory extends TransactionFactory {
	
	private static ScriptEngineManager manager = new ScriptEngineManager();
	private ScriptEngine engine;
	
	public ScriptTransactionFactory(Book book) throws ScriptException {
		super(book);
			
        engine = manager.getEngineByName("JavaScript");
        engine.eval("importPackage(Packages.corporation.model.bookkeeping);");
        engine.eval("importPackage(Packages.corporation.model.bookkeeping.convenience);");

        engine.put("book", book);
        engine.put("tf", this);
        engine.put("tr", transaction);
        
	}

	public void eval(Reader reader) throws ScriptException {
		engine.eval(reader);
	}
	
	public void eval(String script) throws ScriptException {
		engine.eval(new StringReader(script));
	}
	
	public void importTransactionData(Transaction transaction) {
		engine.put("dateNoticed", transaction.getDateNoticed());
		engine.put("dateOccurred", transaction.getDateOccurred());
		engine.put("otherParty", transaction.getOtherParty());
		engine.put("description", transaction.getDescription());
		engine.put("baseDocument", transaction.getBaseDocument());
		engine.put("amount", transaction.getParts().get(0).getAmount());
	}
	
	public Iterator<Entry<String, Object>> getVars() {
		Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
		Set<Entry<String, Object>> set = bindings.entrySet();
		return set.iterator();
	}
}
