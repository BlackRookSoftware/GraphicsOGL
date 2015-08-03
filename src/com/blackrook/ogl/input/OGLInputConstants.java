/*******************************************************************************
 * Copyright (c) 2014-2015 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.ogl.input;

/**
 * A class that contains the input key mappings that the OGL framework uses.
 * These key codes are actually analogs of Swing VK constants.
 * @author Matthew Tropiano
 */
public interface OGLInputConstants
{
	/** Key constant for ENTER. */
	public static final int KEY_ENTER = 10;
	/** Key constant for BACK_SPACE. */
	public static final int KEY_BACK_SPACE = 8;
	/** Key constant for TAB. */
	public static final int KEY_TAB = 9;
	/** Key constant for CANCEL. */
	public static final int KEY_CANCEL = 3;
	/** Key constant for CLEAR. */
	public static final int KEY_CLEAR = 12;
	/** Key constant for SHIFT. */
	public static final int KEY_SHIFT = 16;
	/** Key constant for CONTROL. */
	public static final int KEY_CONTROL = 17;
	/** Key constant for ALT. */
	public static final int KEY_ALT = 18;
	/** Key constant for PAUSE. */
	public static final int KEY_PAUSE = 19;
	/** Key constant for CAPS_LOCK. */
	public static final int KEY_CAPS_LOCK = 20;
	/** Key constant for ESCAPE. */
	public static final int KEY_ESCAPE = 27;
	/** Key constant for SPACE. */
	public static final int KEY_SPACE = 32;
	/** Key constant for PAGE_UP. */
	public static final int KEY_PAGE_UP = 33;
	/** Key constant for PAGE_DOWN. */
	public static final int KEY_PAGE_DOWN = 34;
	/** Key constant for END. */
	public static final int KEY_END = 35;
	/** Key constant for HOME. */
	public static final int KEY_HOME = 36;
	/** Key constant for LEFT. */
	public static final int KEY_LEFT = 37;
	/** Key constant for UP. */
	public static final int KEY_UP = 38;
	/** Key constant for RIGHT. */
	public static final int KEY_RIGHT = 39;
	/** Key constant for DOWN. */
	public static final int KEY_DOWN = 40;
	/** Key constant for COMMA. */
	public static final int KEY_COMMA = 44;
	/** Key constant for MINUS. */
	public static final int KEY_MINUS = 45;
	/** Key constant for PERIOD. */
	public static final int KEY_PERIOD = 46;
	/** Key constant for SLASH. */
	public static final int KEY_SLASH = 47;
	/** Key constant for 0. */
	public static final int KEY_0 = 48;
	/** Key constant for 1. */
	public static final int KEY_1 = 49;
	/** Key constant for 2. */
	public static final int KEY_2 = 50;
	/** Key constant for 3. */
	public static final int KEY_3 = 51;
	/** Key constant for 4. */
	public static final int KEY_4 = 52;
	/** Key constant for 5. */
	public static final int KEY_5 = 53;
	/** Key constant for 6. */
	public static final int KEY_6 = 54;
	/** Key constant for 7. */
	public static final int KEY_7 = 55;
	/** Key constant for 8. */
	public static final int KEY_8 = 56;
	/** Key constant for 9. */
	public static final int KEY_9 = 57;
	/** Key constant for SEMICOLON. */
	public static final int KEY_SEMICOLON = 59;
	/** Key constant for EQUALS. */
	public static final int KEY_EQUALS = 61;
	/** Key constant for A. */
	public static final int KEY_A = 65;
	/** Key constant for B. */
	public static final int KEY_B = 66;
	/** Key constant for C. */
	public static final int KEY_C = 67;
	/** Key constant for D. */
	public static final int KEY_D = 68;
	/** Key constant for E. */
	public static final int KEY_E = 69;
	/** Key constant for F. */
	public static final int KEY_F = 70;
	/** Key constant for G. */
	public static final int KEY_G = 71;
	/** Key constant for H. */
	public static final int KEY_H = 72;
	/** Key constant for I. */
	public static final int KEY_I = 73;
	/** Key constant for J. */
	public static final int KEY_J = 74;
	/** Key constant for K. */
	public static final int KEY_K = 75;
	/** Key constant for L. */
	public static final int KEY_L = 76;
	/** Key constant for M. */
	public static final int KEY_M = 77;
	/** Key constant for N. */
	public static final int KEY_N = 78;
	/** Key constant for O. */
	public static final int KEY_O = 79;
	/** Key constant for P. */
	public static final int KEY_P = 80;
	/** Key constant for Q. */
	public static final int KEY_Q = 81;
	/** Key constant for R. */
	public static final int KEY_R = 82;
	/** Key constant for S. */
	public static final int KEY_S = 83;
	/** Key constant for T. */
	public static final int KEY_T = 84;
	/** Key constant for U. */
	public static final int KEY_U = 85;
	/** Key constant for V. */
	public static final int KEY_V = 86;
	/** Key constant for W. */
	public static final int KEY_W = 87;
	/** Key constant for X. */
	public static final int KEY_X = 88;
	/** Key constant for Y. */
	public static final int KEY_Y = 89;
	/** Key constant for Z. */
	public static final int KEY_Z = 90;
	/** Key constant for OPEN_BRACKET. */
	public static final int KEY_OPEN_BRACKET = 91;
	/** Key constant for BACK_SLASH. */
	public static final int KEY_BACK_SLASH = 92;
	/** Key constant for CLOSE_BRACKET. */
	public static final int KEY_CLOSE_BRACKET = 93;
	/** Key constant for NUMPAD0. */
	public static final int KEY_NUMPAD0 = 96;
	/** Key constant for NUMPAD1. */
	public static final int KEY_NUMPAD1 = 97;
	/** Key constant for NUMPAD2. */
	public static final int KEY_NUMPAD2 = 98;
	/** Key constant for NUMPAD3. */
	public static final int KEY_NUMPAD3 = 99;
	/** Key constant for NUMPAD4. */
	public static final int KEY_NUMPAD4 = 100;
	/** Key constant for NUMPAD5. */
	public static final int KEY_NUMPAD5 = 101;
	/** Key constant for NUMPAD6. */
	public static final int KEY_NUMPAD6 = 102;
	/** Key constant for NUMPAD7. */
	public static final int KEY_NUMPAD7 = 103;
	/** Key constant for NUMPAD8. */
	public static final int KEY_NUMPAD8 = 104;
	/** Key constant for NUMPAD9. */
	public static final int KEY_NUMPAD9 = 105;
	/** Key constant for MULTIPLY. */
	public static final int KEY_MULTIPLY = 106;
	/** Key constant for ADD. */
	public static final int KEY_ADD = 107;
	/** Key constant for SEPARATER. */
	public static final int KEY_SEPARATER = 108;
	/** Key constant for SEPARATOR. */
	public static final int KEY_SEPARATOR = 108;
	/** Key constant for SUBTRACT. */
	public static final int KEY_SUBTRACT = 109;
	/** Key constant for DECIMAL. */
	public static final int KEY_DECIMAL = 110;
	/** Key constant for DIVIDE. */
	public static final int KEY_DIVIDE = 111;
	/** Key constant for DELETE. */
	public static final int KEY_DELETE = 127;
	/** Key constant for NUM_LOCK. */
	public static final int KEY_NUM_LOCK = 144;
	/** Key constant for SCROLL_LOCK. */
	public static final int KEY_SCROLL_LOCK = 145;
	/** Key constant for F1. */
	public static final int KEY_F1 = 112;
	/** Key constant for F2. */
	public static final int KEY_F2 = 113;
	/** Key constant for F3. */
	public static final int KEY_F3 = 114;
	/** Key constant for F4. */
	public static final int KEY_F4 = 115;
	/** Key constant for F5. */
	public static final int KEY_F5 = 116;
	/** Key constant for F6. */
	public static final int KEY_F6 = 117;
	/** Key constant for F7. */
	public static final int KEY_F7 = 118;
	/** Key constant for F8. */
	public static final int KEY_F8 = 119;
	/** Key constant for F9. */
	public static final int KEY_F9 = 120;
	/** Key constant for F10. */
	public static final int KEY_F10 = 121;
	/** Key constant for F11. */
	public static final int KEY_F11 = 122;
	/** Key constant for F12. */
	public static final int KEY_F12 = 123;
	/** Key constant for F13. */
	public static final int KEY_F13 = 61440;
	/** Key constant for F14. */
	public static final int KEY_F14 = 61441;
	/** Key constant for F15. */
	public static final int KEY_F15 = 61442;
	/** Key constant for F16. */
	public static final int KEY_F16 = 61443;
	/** Key constant for F17. */
	public static final int KEY_F17 = 61444;
	/** Key constant for F18. */
	public static final int KEY_F18 = 61445;
	/** Key constant for F19. */
	public static final int KEY_F19 = 61446;
	/** Key constant for F20. */
	public static final int KEY_F20 = 61447;
	/** Key constant for F21. */
	public static final int KEY_F21 = 61448;
	/** Key constant for F22. */
	public static final int KEY_F22 = 61449;
	/** Key constant for F23. */
	public static final int KEY_F23 = 61450;
	/** Key constant for F24. */
	public static final int KEY_F24 = 61451;
	/** Key constant for PRINTSCREEN. */
	public static final int KEY_PRINTSCREEN = 154;
	/** Key constant for INSERT. */
	public static final int KEY_INSERT = 155;
	/** Key constant for HELP. */
	public static final int KEY_HELP = 156;
	/** Key constant for META. */
	public static final int KEY_META = 157;
	/** Key constant for BACK_QUOTE. */
	public static final int KEY_BACK_QUOTE = 192;
	/** Key constant for QUOTE. */
	public static final int KEY_QUOTE = 222;
	/** Key constant for KP_UP. */
	public static final int KEY_KP_UP = 224;
	/** Key constant for KP_DOWN. */
	public static final int KEY_KP_DOWN = 225;
	/** Key constant for KP_LEFT. */
	public static final int KEY_KP_LEFT = 226;
	/** Key constant for KP_RIGHT. */
	public static final int KEY_KP_RIGHT = 227;
	/** Key constant for DEAD_GRAVE. */
	public static final int KEY_DEAD_GRAVE = 128;
	/** Key constant for DEAD_ACUTE. */
	public static final int KEY_DEAD_ACUTE = 129;
	/** Key constant for DEAD_CIRCUMFLEX. */
	public static final int KEY_DEAD_CIRCUMFLEX = 130;
	/** Key constant for DEAD_TILDE. */
	public static final int KEY_DEAD_TILDE = 131;
	/** Key constant for DEAD_MACRON. */
	public static final int KEY_DEAD_MACRON = 132;
	/** Key constant for DEAD_BREVE. */
	public static final int KEY_DEAD_BREVE = 133;
	/** Key constant for DEAD_ABOVEDOT. */
	public static final int KEY_DEAD_ABOVEDOT = 134;
	/** Key constant for DEAD_DIAERESIS. */
	public static final int KEY_DEAD_DIAERESIS = 135;
	/** Key constant for DEAD_ABOVERING. */
	public static final int KEY_DEAD_ABOVERING = 136;
	/** Key constant for DEAD_DOUBLEACUTE. */
	public static final int KEY_DEAD_DOUBLEACUTE = 137;
	/** Key constant for DEAD_CARON. */
	public static final int KEY_DEAD_CARON = 138;
	/** Key constant for DEAD_CEDILLA. */
	public static final int KEY_DEAD_CEDILLA = 139;
	/** Key constant for DEAD_OGONEK. */
	public static final int KEY_DEAD_OGONEK = 140;
	/** Key constant for DEAD_IOTA. */
	public static final int KEY_DEAD_IOTA = 141;
	/** Key constant for DEAD_VOICED_SOUND. */
	public static final int KEY_DEAD_VOICED_SOUND = 142;
	/** Key constant for DEAD_SEMIVOICED_SOUND. */
	public static final int KEY_DEAD_SEMIVOICED_SOUND = 143;
	/** Key constant for AMPERSAND. */
	public static final int KEY_AMPERSAND = 150;
	/** Key constant for ASTERISK. */
	public static final int KEY_ASTERISK = 151;
	/** Key constant for QUOTEDBL. */
	public static final int KEY_QUOTEDBL = 152;
	/** Key constant for LESS. */
	public static final int KEY_LESS = 153;
	/** Key constant for GREATER. */
	public static final int KEY_GREATER = 160;
	/** Key constant for BRACELEFT. */
	public static final int KEY_BRACELEFT = 161;
	/** Key constant for BRACERIGHT. */
	public static final int KEY_BRACERIGHT = 162;
	/** Key constant for AT. */
	public static final int KEY_AT = 512;
	/** Key constant for COLON. */
	public static final int KEY_COLON = 513;
	/** Key constant for CIRCUMFLEX. */
	public static final int KEY_CIRCUMFLEX = 514;
	/** Key constant for DOLLAR. */
	public static final int KEY_DOLLAR = 515;
	/** Key constant for EURO_SIGN. */
	public static final int KEY_EURO_SIGN = 516;
	/** Key constant for EXCLAMATION_MARK. */
	public static final int KEY_EXCLAMATION_MARK = 517;
	/** Key constant for INVERTED_EXCLAMATION_MARK. */
	public static final int KEY_INVERTED_EXCLAMATION_MARK = 518;
	/** Key constant for LEFT_PARENTHESIS. */
	public static final int KEY_LEFT_PARENTHESIS = 519;
	/** Key constant for NUMBER_SIGN. */
	public static final int KEY_NUMBER_SIGN = 520;
	/** Key constant for PLUS. */
	public static final int KEY_PLUS = 521;
	/** Key constant for RIGHT_PARENTHESIS. */
	public static final int KEY_RIGHT_PARENTHESIS = 522;
	/** Key constant for UNDERSCORE. */
	public static final int KEY_UNDERSCORE = 523;
	/** Key constant for WINDOWS. */
	public static final int KEY_WINDOWS = 524;
	/** Key constant for CONTEXT_MENU. */
	public static final int KEY_CONTEXT_MENU = 525;
	/** Key constant for FINAL. */
	public static final int KEY_FINAL = 24;
	/** Key constant for CONVERT. */
	public static final int KEY_CONVERT = 28;
	/** Key constant for NONCONVERT. */
	public static final int KEY_NONCONVERT = 29;
	/** Key constant for ACCEPT. */
	public static final int KEY_ACCEPT = 30;
	/** Key constant for MODECHANGE. */
	public static final int KEY_MODECHANGE = 31;
	/** Key constant for KANA. */
	public static final int KEY_KANA = 21;
	/** Key constant for KANJI. */
	public static final int KEY_KANJI = 25;
	/** Key constant for ALPHANUMERIC. */
	public static final int KEY_ALPHANUMERIC = 240;
	/** Key constant for KATAKANA. */
	public static final int KEY_KATAKANA = 241;
	/** Key constant for HIRAGANA. */
	public static final int KEY_HIRAGANA = 242;
	/** Key constant for FULL_WIDTH. */
	public static final int KEY_FULL_WIDTH = 243;
	/** Key constant for HALF_WIDTH. */
	public static final int KEY_HALF_WIDTH = 244;
	/** Key constant for ROMAN_CHARACTERS. */
	public static final int KEY_ROMAN_CHARACTERS = 245;
	/** Key constant for ALL_CANDIDATES. */
	public static final int KEY_ALL_CANDIDATES = 256;
	/** Key constant for PREVIOUS_CANDIDATE. */
	public static final int KEY_PREVIOUS_CANDIDATE = 257;
	/** Key constant for CODE_INPUT. */
	public static final int KEY_CODE_INPUT = 258;
	/** Key constant for JAPANESE_KATAKANA. */
	public static final int KEY_JAPANESE_KATAKANA = 259;
	/** Key constant for JAPANESE_HIRAGANA. */
	public static final int KEY_JAPANESE_HIRAGANA = 260;
	/** Key constant for JAPANESE_ROMAN. */
	public static final int KEY_JAPANESE_ROMAN = 261;
	/** Key constant for KANA_LOCK. */
	public static final int KEY_KANA_LOCK = 262;
	/** Key constant for INPUT_METHOD_ON_OFF. */
	public static final int KEY_INPUT_METHOD_ON_OFF = 263;
	/** Key constant for CUT. */
	public static final int KEY_CUT = 65489;
	/** Key constant for COPY. */
	public static final int KEY_COPY = 65485;
	/** Key constant for PASTE. */
	public static final int KEY_PASTE = 65487;
	/** Key constant for UNDO. */
	public static final int KEY_UNDO = 65483;
	/** Key constant for AGAIN. */
	public static final int KEY_AGAIN = 65481;
	/** Key constant for FIND. */
	public static final int KEY_FIND = 65488;
	/** Key constant for PROPS. */
	public static final int KEY_PROPS = 65482;
	/** Key constant for STOP. */
	public static final int KEY_STOP = 65480;
	/** Key constant for COMPOSE. */
	public static final int KEY_COMPOSE = 65312;
	/** Key constant for ALT_GRAPH. */
	public static final int KEY_ALT_GRAPH = 65406;
	/** Key constant for BEGIN. */
	public static final int KEY_BEGIN = 65368;
	/** Key constant for UNDEFINED. */
	public static final int KEY_UNDEFINED = 0;
	
	/** Mouse button constant for an unknown button. */
	public static final int MOUSE_UNDEFINED = 0;
	/** Mouse button constant for the LEFT button. */
	public static final int MOUSE_LEFT = 1;
	/** Mouse button constant for the RIGHT button. */
	public static final int MOUSE_RIGHT = 2;
	/** Mouse button constant for the CENTER button. */
	public static final int MOUSE_CENTER = 3;
	/** Mouse button constant for the BACK button. */
	public static final int MOUSE_BACK = 4;
	/** Mouse button constant for the FORWARD button. */
	public static final int MOUSE_FORWARD = 5;
	/** Mouse button constant for the 6th button. */
	public static final int MOUSE_BUTTON6 = 6;
	/** Mouse button constant for the 7th button. */
	public static final int MOUSE_BUTTON7 = 7;
	/** Mouse button constant for the 8th button. */
	public static final int MOUSE_BUTTON8 = 8;
	/** Mouse button constant for the 9th button. */
	public static final int MOUSE_BUTTON9 = 9;
	/** Mouse button constant for the 10th button. */
	public static final int MOUSE_BUTTON10 = 10;
	/** Mouse button constant for the 11th button. */
	public static final int MOUSE_BUTTON11 = 11;
	/** Mouse button constant for the 12th button. */
	public static final int MOUSE_BUTTON12 = 12;
	
	/** Gamepad axis type: Undefined axis. */
	public static final int AXIS_UNDEFINED = 0;
	/** Gamepad axis type: X axis. */
	public static final int AXIS_X = 1;
	/** Gamepad axis type: Y axis. */
	public static final int AXIS_Y = 2;
	/** Gamepad axis type: Z axis. */
	public static final int AXIS_Z = 3;
	/** Gamepad axis type: RX axis. */
	public static final int AXIS_RX = 4;
	/** Gamepad axis type: RY axis. */
	public static final int AXIS_RY = 5;
	/** Gamepad axis type: RZ axis. */
	public static final int AXIS_RZ = 6;
	/** Gamepad axis type: Slider. */
	public static final int AXIS_SLIDER = 7;
	/** Gamepad axis type: POV Hat. */
	public static final int AXIS_POV = 8;
	
	/** Gamepad POV Hat value: UP LEFT. */
	public static final float AXIS_POV_UP_LEFT = 0.125f;
	/** Gamepad POV Hat value: UP. */
	public static final float AXIS_POV_UP = 0.25f;
	/** Gamepad POV Hat value: UP RIGHT. */
	public static final float AXIS_POV_UP_RIGHT = 0.375f;
	/** Gamepad POV Hat value: RIGHT. */
	public static final float AXIS_POV_RIGHT = 0.5f;
	/** Gamepad POV Hat value: DOWN RIGHT. */
	public static final float AXIS_POV_DOWN_RIGHT = 0.625f;
	/** Gamepad POV Hat value: DOWN. */
	public static final float AXIS_POV_DOWN = 0.75f;
	/** Gamepad POV Hat value: DOWN LEFT. */
	public static final float AXIS_POV_DOWN_LEFT = 0.875f;
	/** Gamepad POV Hat value: LEFT. */
	public static final float AXIS_POV_LEFT = 1f;

	/** Gamepad XBOX mapping: Axis Left Stick X. */
	public static final int AXIS_XBOX_LEFT_X = AXIS_X;
	/** Gamepad XBOX mapping: Axis Left Stick Y. */
	public static final int AXIS_XBOX_LEFT_Y = AXIS_Y;
	/** Gamepad XBOX mapping: Axis Right Stick X. */
	public static final int AXIS_XBOX_RIGHT_X = AXIS_RX;
	/** Gamepad XBOX mapping: Axis Right Stick Y. */
	public static final int AXIS_XBOX_RIGHT_Y = AXIS_RY;
	/** Gamepad XBOX mapping: Axis Triggers. */
	public static final int AXIS_XBOX_TRIGGER = AXIS_Z;
	/** Gamepad XBOX mapping: Axis Control Pad (POV). */
	public static final int AXIS_XBOX_PAD = AXIS_POV;
	
	/** Gamepad button: undefined. */
	public static final int GAMEPAD_UNDEFINED = 0;
	/** Gamepad button 1. */
	public static final int GAMEPAD_1 = 1;
	/** Gamepad button 2. */
	public static final int GAMEPAD_2 = 2;
	/** Gamepad button 3. */
	public static final int GAMEPAD_3 = 3;
	/** Gamepad button 4. */
	public static final int GAMEPAD_4 = 4;
	/** Gamepad button 5. */
	public static final int GAMEPAD_5 = 5;
	/** Gamepad button 6. */
	public static final int GAMEPAD_6 = 6;
	/** Gamepad button 7. */
	public static final int GAMEPAD_7 = 7;
	/** Gamepad button 8. */
	public static final int GAMEPAD_8 = 8;
	/** Gamepad button 9. */
	public static final int GAMEPAD_9 = 9;
	/** Gamepad button 10. */
	public static final int GAMEPAD_10 = 10;
	/** Gamepad button 11. */
	public static final int GAMEPAD_11 = 11;
	/** Gamepad button 12. */
	public static final int GAMEPAD_12 = 12;

	/** Gamepad XBOX mapping: Button A. */
	public static final int GAMEPAD_XBOX_A = GAMEPAD_1;
	/** Gamepad XBOX mapping: Button B. */
	public static final int GAMEPAD_XBOX_B = GAMEPAD_2;
	/** Gamepad XBOX mapping: Button X. */
	public static final int GAMEPAD_XBOX_X = GAMEPAD_3;
	/** Gamepad XBOX mapping: Button Y. */
	public static final int GAMEPAD_XBOX_Y = GAMEPAD_4;
	/** Gamepad XBOX mapping: Button Left Bumper. */
	public static final int GAMEPAD_XBOX_LB = GAMEPAD_5;
	/** Gamepad XBOX mapping: Button Right Bumper. */
	public static final int GAMEPAD_XBOX_RB = GAMEPAD_6;
	/** Gamepad XBOX mapping: Button Back. */
	public static final int GAMEPAD_XBOX_BACK = GAMEPAD_7;
	/** Gamepad XBOX mapping: ButtonStart. */
	public static final int GAMEPAD_XBOX_START = GAMEPAD_8;
	/** Gamepad XBOX mapping: Button Left Stick. */
	public static final int GAMEPAD_XBOX_LSTICK = GAMEPAD_9;
	/** Gamepad XBOX mapping: Button Right Stick. */
	public static final int GAMEPAD_XBOX_RSTICK = GAMEPAD_10;

}
