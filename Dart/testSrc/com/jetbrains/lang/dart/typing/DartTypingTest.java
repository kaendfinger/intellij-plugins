package com.jetbrains.lang.dart.typing;

import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.util.text.StringUtil;
import com.jetbrains.lang.dart.DartCodeInsightFixtureTestCase;
import com.jetbrains.lang.dart.DartFileType;
import org.jetbrains.annotations.NotNull;

public class DartTypingTest extends DartCodeInsightFixtureTestCase {
  @Override
  protected String getBasePath() {
    return "/typing";
  }

  protected void doTest(char charToType) throws Throwable {
    myFixture.configureByFiles(getTestName(false) + ".dart");
    myFixture.type(charToType);
    myFixture.checkResultByFile(getTestName(false) + "_after.dart");
  }

  private void doTypingTest(final char charToType, final @NotNull String textBefore, final @NotNull String textAfter) {
    myFixture.configureByText(DartFileType.INSTANCE, textBefore);
    myFixture.type(charToType);
    myFixture.checkResult(textAfter);
  }

  private void doBackspaceTest(final @NotNull String textBefore, final @NotNull String textAfter) {
    myFixture.configureByText(DartFileType.INSTANCE, textBefore);
    myFixture.performEditorAction(IdeActions.ACTION_EDITOR_BACKSPACE);
    myFixture.checkResult(textAfter);
  }

  public void testDocComment() throws Throwable {
    doTest('\n');
  }

  public void testDocComment2() throws Throwable {
    doTest('\n');
  }

  public void testDocComment3() throws Throwable {
    doTest('\n');
  }

  public void testGenericBraceWithMultiCaret() throws Throwable {
    doTest('<');
  }

  public void testGenericBraceWithMultiCaretInDifferentContexts() throws Throwable {
    doTest('<');
  }

  public void testGenericBrace1() throws Throwable {
    doTest('<');
  }

  public void testGenericBrace2() throws Throwable {
    doTest('<');
  }

  public void testGenericBrace3() throws Throwable {
    doTest('<');
  }

  public void testLess() throws Throwable {
    doTest('<');
  }

  public void testStringWithMultiCaret() throws Throwable {
    doTest('{');
  }

  public void testStringWithMultiCaretInDifferentContexts() throws Throwable {
    doTest('{');
  }

  public void testString1() throws Throwable {
    doTest('{');
  }

  public void testString2() throws Throwable {
    doTest('{');
  }

  public void testString3() throws Throwable {
    doTest('{');
  }

  public void testQuote() throws Throwable {
    doTypingTest('\'', "var foo = <caret>", "var foo = '<caret>'");
    doTypingTest('"', "var foo = <caret>", "var foo = \"<caret>\"");
    doTypingTest('"', "var foo = '<caret>'", "var foo = '\"<caret>'");
    doTypingTest('\'', "var foo = \"<caret>\"", "var foo = \"'<caret>\"");
    doTypingTest('\'', "var foo = \"bar<caret>\"", "var foo = \"bar'<caret>\"");
    doTypingTest('\'', "import <caret>", "import '<caret>'");
    doTypingTest('"', "import <caret>", "import \"<caret>\"");
    doTypingTest('\'', "var foo = '<caret>'", "var foo = ''<caret>");
    doTypingTest('\"', "var foo = \"<caret>\"", "var foo = \"\"<caret>");
    doTypingTest('\'', "var foo = 'bar<caret>'", "var foo = 'bar'<caret>");
    doTypingTest('\"', "var foo = \"bar<caret>\"", "var foo = \"bar\"<caret>");
    doTypingTest('\'', "var foo = 'bar' <caret>", "var foo = 'bar' '<caret>'");
    doTypingTest('\"', "var foo = \"\" <caret>", "var foo = \"\" \"<caret>\"");
  }

  public void testBackspace() throws Throwable {
    doBackspaceTest("var foo = \"<caret> \"", "var foo = <caret> \"");
    doBackspaceTest("var foo = \"<caret>\"", "var foo = <caret>");
    doBackspaceTest("var foo = '<caret>a'", "var foo = <caret>a'");
    doBackspaceTest("import '<caret>'", "import <caret>");
    doBackspaceTest("var foo = \"\"<caret>", "var foo = \"<caret>");
    doBackspaceTest("var foo = \" '<caret>' \"", "var foo = \" <caret>' \"");
    doBackspaceTest("var foo = '\"<caret>\"'", "var foo = '<caret>\"'");
  }

  public void testWEB_8315() throws Throwable {
    doTypingTest('\n',
                 "class X {\n" +
                 "  num x;<caret>\n" +
                 "}",
                 "class X {\n" +
                 "  num x;\n" +
                 "  <caret>\n" +
                 "}");
  }

  public void testCaseAlignAfterColon1() throws Throwable {
    doTypingTest(':',
                 "class X {\n" +
                 "  void doit(x) {\n" +
                 "    switch (x) {\n" +
                 "      case 1<caret>\n" +
                 "    }\n" +
                 "  }\n" +
                 "}",
                 "class X {\n" +
                 "  void doit(x) {\n" +
                 "    switch (x) {\n" +
                 "      case 1:<caret>\n" +
                 "    }\n" +
                 "  }\n" +
                 "}");
  }

  public void testCaseAlignAfterColon2() throws Throwable {
    doTypingTest(':',
                 "class X {\n" +
                 "  void doit(x) {\n" +
                 "    switch (x) {\n" +
                 "      case 1:\n" +
                 "    case 2<caret>\n" +
                 "    }\n" +
                 "  }\n" +
                 "}",
                 "class X {\n" +
                 "  void doit(x) {\n" +
                 "    switch (x) {\n" +
                 "      case 1:\n" +
                 "      case 2:<caret>\n" +
                 "    }\n" +
                 "  }\n" +
                 "}");
  }

  public void testDefaultAlignAfterColon() throws Throwable {
    doTypingTest(':',
                 "class X {\n" +
                 "  void doit(x) {\n" +
                 "    switch (x) {\n" +
                 "      case 1:\n" +
                 "    default<caret>\n" +
                 "    }\n" +
                 "  }\n" +
                 "}",
                 "class X {\n" +
                 "  void doit(x) {\n" +
                 "    switch (x) {\n" +
                 "      case 1:\n" +
                 "      default:<caret>\n" +
                 "    }\n" +
                 "  }\n" +
                 "}");
  }

  public void testCaseStringAlignAfterColon() throws Throwable {
    doTypingTest(':',
                 "class X {\n" +
                 "  void doit(x) {\n" +
                 "    switch (x) {\n" +
                 "      case 1:\n" +
                 "    case '<caret>'\n" +
                 "    }\n" +
                 "  }\n" +
                 "}",
                 "class X {\n" +
                 "  void doit(x) {\n" +
                 "    switch (x) {\n" +
                 "      case 1:\n" +
                 "    case ':<caret>'\n" +
                 "    }\n" +
                 "  }\n" +
                 "}");
  }

  public void testEnterInSwitch() throws Throwable {
    doTypingTest('\n',
                 "void bar() {\n" +
                 "  switch (1) {<caret>\n" +
                 "}",
                 "void bar() {\n" +
                 "  switch (1) {\n" +
                 "    <caret>\n" +
                 "  }\n" +
                 "}");
  }

  public void testEnterAfterCase() throws Throwable {
    doTypingTest('\n',
                 "void bar() {\n" +
                 "  switch (1) {\n" +
                 "    case 1+1: <caret>\n" +
                 "      a;\n" +
                 "    case 2:\n" +
                 "  }\n" +
                 "}",
                 "void bar() {\n" +
                 "  switch (1) {\n" +
                 "    case 1+1: \n" +
                 "      <caret>\n" +
                 "      a;\n" +
                 "    case 2:\n" +
                 "  }\n" +
                 "}");
  }

  public void testEnterAfterDefault() throws Throwable {
    doTypingTest('\n',
                 "void bar() {\n" +
                 "  switch (1) {\n" +
                 "    case 1:\n" +
                 "    default:<caret>\n" +
                 "  }\n" +
                 "}",
                 "void bar() {\n" +
                 "  switch (1) {\n" +
                 "    case 1:\n" +
                 "    default:\n" +
                 "      <caret>\n" +
                 "  }\n" +
                 "}");
  }

  public void testEnterAfterBreakInCase() throws Throwable {
    final String textBefore = "void bar() {\n" +
                              "  switch (1) {\n" +
                              "    case 1:\n" +
                              "      break;<caret>\n" +
                              "  }\n" +
                              "}";
    final String textAfter = "void bar() {\n" +
                             "  switch (1) {\n" +
                             "    case 1:\n" +
                             "      break;\n" +
                             "    <caret>\n" +
                             "  }\n" +
                             "}";

    doTypingTest('\n', textBefore, textAfter);
    doTypingTest('\n', StringUtil.replace(textBefore, "break;", "continue;"), StringUtil.replace(textAfter, "break;", "continue;"));
    doTypingTest('\n', StringUtil.replace(textBefore, "break;", "return 1+1;"), StringUtil.replace(textAfter, "break;", "return 1+1;"));
    doTypingTest('\n', StringUtil.replace(textBefore, "break;", "throw '';"), StringUtil.replace(textAfter, "break;", "throw '';"));
    doTypingTest('\n', StringUtil.replace(textBefore, "break;", "foo;"),
                 StringUtil.replace(textAfter, "break;\n    <caret>", "foo;\n      <caret>"));
  }

  public void testEnterInMapLiteral() throws Throwable {
    doTypingTest('\n', "var data = {<caret>};", "var data = {\n" +
                                                "  <caret>\n" +
                                                "};");
    doTypingTest('\n',
                 "var data = {\n" +
                 "  1:1,<caret>\n" +
                 "};",
                 "var data = {\n" +
                 "  1:1,\n" +
                 "  <caret>\n" +
                 "};");
  }

  public void testEnterInListLiteral() throws Throwable {
    doTypingTest('\n',
                 "var data = [<caret>\n" +
                 "];",
                 "var data = [\n" +
                 "  <caret>\n" +
                 "];");
    doTypingTest('\n',
                 "var data = [\n" +
                 "  1,<caret>\n" +
                 "];",
                 "var data = [\n" +
                 "  1,\n" +
                 "  <caret>\n" +
                 "];");
  }

  public void testLt() {
    doTypingTest('<', "Map<List<caret>>", "Map<List<<caret>>>");
    doTypingTest('<', "class A<caret>", "class A<<caret>>");
  }

  public void testGt() {
    doTypingTest('>', "foo () {Map<List<<caret>>}", "foo () {Map<List<><caret>>}");
    doTypingTest('>', "Map<List<<caret>>>", "Map<List<><caret>>");
    doTypingTest('>', "Map<List<><caret>>", "Map<List<>><caret>");
    doTypingTest('>', "Map<List<><caret>", "Map<List<>><caret>");
    doTypingTest('>', "Map<List<>><caret>", "Map<List<>>><caret>");
    doTypingTest('>', "Map<List<A>, B <caret>", "Map<List<A>, B ><caret>");
    doTypingTest('>', "class A<T, E <caret>", "class A<T, E ><caret>");
    doTypingTest('>', "class A<T, E <caret>>", "class A<T, E ><caret>");
  }

  public void testLBraceInString() {
    doTypingTest('{', "var a = 'xx$<caret>xx'", "var a = 'xx${<caret>}xx'");
    doTypingTest('{', "foo () {var a = 'xx$<caret>xx';\n}", "foo () {var a = 'xx${<caret>}xx';\n}");
    doTypingTest('{', "var a = \"$<caret>\";", "var a = \"${<caret>}\";");
    doTypingTest('{', "var a = r'$<caret>'", "var a = r'${<caret>'");
    doTypingTest('{', "var a = '''$<caret>'''", "var a = '''${<caret>}'''");
    doTypingTest('{', "var a = '${}<caret>'", "var a = '${}{<caret>'");
    doTypingTest('{', "<caret>", "{<caret>}");
  }

  public void testRBraceInString() {
    doTypingTest('}', "var a = 'xx${<caret>}xx'", "var a = 'xx${}<caret>xx'");
    doTypingTest('}', "var a = 'xx${<caret>xx'", "var a = 'xx${}<caret>xx'");
    doTypingTest('}', "var a = \"${1 + 2 <caret>}\"", "var a = \"${1 + 2 }<caret>\"");
    doTypingTest('}', "var a = r'${<caret>}'", "var a = r'${}<caret>}'");
    doTypingTest('}', "var a = '''${<caret>}'''", "var a = '''${}<caret>'''");
    doTypingTest('}', "var a = '${{<caret>}}'", "var a = '${{}<caret>}'");
    doTypingTest('}', "var a = '${{a<caret>}'", "var a = '${{a}<caret>'");
    doTypingTest('}', "var a = '${{1+1;}<caret>}'", "var a = '${{1+1;}}<caret>'");
    doTypingTest('}', "var a = '${{}<caret>'", "var a = '${{}}<caret>'");
    doTypingTest('}', "var a = '${{}}<caret>}'", "var a = '${{}}}<caret>}'");
  }

  public void testEnterInSingleLineDocComment() {
    doTypingTest('\n', "///<caret>", "///\n/// <caret>");
    doTypingTest('\n', "///     q<caret>", "///     q\n///     <caret>");
    doTypingTest('\n', "///     <caret>q", "///     \n///     <caret>q");
    doTypingTest('\n', "/// Hello<caret>Dart", "/// Hello\n/// <caret>Dart");
    doTypingTest('\n', "///   q  <caret>", "///   q  \n///   <caret>");
    doTypingTest('\n', "///   q  <caret>z", "///   q  \n///   <caret>z");
    doTypingTest('\n', "///   q  <caret>  z", "///   q  \n///   <caret>z");
    doTypingTest('\n', "///   q<caret>   z", "///   q\n///   <caret>z");
    doTypingTest('\n', "  ///   q  <caret>    z", "  ///   q  \n  ///   <caret> z");
    doTypingTest('\n', "///q<caret>z", "///q\n///<caret>z");
    doTypingTest('\n', " ///q<caret> \t ///z", " ///q \t \n ///<caret>z");
  }
}
