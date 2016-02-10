class Row:
	def __init__(self,symId,symType,symClass,symAttrib):
		self.symId = symId;
		self.symType = symType;
		self.symClass = symClass;
		self.symAttrib = symAttrib
		
		

class SingleSymbolTable:
	def __init__(self):
		self.symbols = []
		pass

	def insert(self,symId,symType,symClass,symAttrib):
		'''Returns the row inserted if it's successfully inserted.
		returns None otherewise'''
		if (self.lookup(symId) == None):
			self.symbols.append(Row(symId,symType,symClass,symAttrib))
			return Row(symId,symType,symClass,symAttrib)
		else:
			raise LookupError("Symbol '" + symId + "' is multiply defined.")
	
	def lookup(self,symId):
		for row in self.symbols:
			if row.symId == symId:
				return row
		return None
	def display(self):
		for row in self.symbols:
			if (row.symClass == "VAR"):
				print row.symId + "\t" + row.symType.type + "\t" + row.symClass + "\t" + str(row.symAttrib)
			elif (row.symClass == "FUN"):
				types = ""
				for thing in row.symAttrib[1]:
					types = types + thing[2].type + ", "
				if len(types) == 0:
					types = ", "
				print row.symId + "\t" + row.symType.type + "\t" + row.symClass + "\t" + "("+row.symAttrib[0]+",["+types[:-2]+"])"

class SymbolTable:
	'''Just a stack of SingleSymbolTables'''
	def __init__(self):
		self.tables = [SingleSymbolTable()]
		pass
	def insert(self,symId,symType,symClass,symAttrib):
		return self.tables[-1].insert(symId,symType,symClass,symAttrib)
	def lookup(self,symId):
		distance = 0;
		for table in reversed(self.tables):
			result = table.lookup(symId)
			if result != None:
				return {"info":result, "level":distance}
			distance += 1
		return None
	def push(self):
		self.tables.append(SingleSymbolTable())
	def pop(self):
		self.tables.pop()
		if len(self.tables) == 0:
			self.tables = [SingleSymbolTable()]
	def display(self):
		'''produces a human-readable representation of the symbol
		table. The top (as in the first thing printed) is the thing
		on top of the stack. SymbolTables are separated by dashes.'''
		for table in reversed(self.tables):
			print "--------------------------------------"
			table.display()