package com.oz.lanslim.message;

import java.io.Serializable;

import com.oz.lanslim.model.SlimUserContact;

public class SlimExitTalkMessage extends SlimTalkMessage implements Serializable {

	public SlimExitTalkMessage(SlimUserContact pSender, String pTalkId) {
		super(pSender, SlimMessageTypeEnum.EXIT_TALK, pTalkId);
	}
	
}
