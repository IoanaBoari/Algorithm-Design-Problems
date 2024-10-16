# Exemplu de Makefile pentru temă. "Rezolvați" doar comentariul de mai jos.

# Numele arhivei generate de comanda pack, pe care o puteți trimite.
ARCHIVE := submission.zip

# Parametri pentru compilare.
CCFLAGS := -std=c++17 -Wall -Wextra -O0 -lm
JFLAGS := -Xlint:unchecked

# Directorul care conține sursele voastre, și cel unde punem binarele.
# Cel mai safe e să le lăsați așa. Dacă schimbați, folosiți path-uri relative!
SRC_DIR := .
OUT_DIR := .

# Compilăm toate sursele găsite în $(SRC_DIR).
# Modificați doar dacă vreți să compilați alte surse.
CC_SRC := $(wildcard $(SRC_DIR)/*.cpp)
JAVA_SRC := $(wildcard $(SRC_DIR)/*.java)

CC_EXECS := $(CC_SRC:$(SRC_DIR)/%.cpp=$(OUT_DIR)/%)
JAVA_CLASSES := $(JAVA_SRC:$(SRC_DIR)/%.java=$(OUT_DIR)/%.class)
TARGETS := $(CC_EXECS) $(JAVA_CLASSES)


.PHONY: build clean pack

build: $(TARGETS)

clean:
	rm -f $(TARGETS) $(OUT_DIR)/*.class $(ARCHIVE)

pack:
	@find $(SRC_DIR) \
		\( -path "./_utils/*" -prune \) -o \
		-regex ".*\.\(cpp\|h\|hpp\|java\)" -exec zip $(ARCHIVE) {} +
	@zip $(ARCHIVE) Makefile
	@[ -f README.md ] && zip $(ARCHIVE) README.md \
		|| echo "You should write README.md!"
	@echo "Created $(ARCHIVE)"


# TODO: Apelați soluția fiecărei probleme. Puteți completa o regulă așa:
# Pentru C++
#	$(OUT_DIR)/<nume_problemă>
# Pentru Java
#	java -cp $(OUT_DIR) <NumeProblemă>
run-p1:
	java -cp $(OUT_DIR) Servere.java
run-p2:
	java -cp $(OUT_DIR) Colorare.java
run-p3:
	java -cp $(OUT_DIR) Compresie.java
run-p4:
	java -cp $(OUT_DIR) Criptat.java
run-p5:
	java -cp $(OUT_DIR) Oferta.java


# Reguli pentru compilare. Probabil nu e nevoie să le modificați.

#$(CC_EXECS): $(OUT_DIR)/%: $(SRC_DIR)/%.cpp
#	g++ -o $@ $^ $(CCFLAGS)

$(JAVA_CLASSES): $(OUT_DIR)/%.class: $(SRC_DIR)/%.java
	javac $< -d $(OUT_DIR) -cp $(SRC_DIR) $(JFLAGS)