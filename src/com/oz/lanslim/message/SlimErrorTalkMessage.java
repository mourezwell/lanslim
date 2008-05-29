package com.oz.lanslim.message;

import java.io.Serializable;

import com.oz.lanslim.model.SlimUserContact;

public class SlimErrorTalkMessage extends SlimTalkMessage implements Serializable {

	public SlimErrorTalkMessage(SlimUserContact pSender, String pTalkId) {
		super(pSender, SlimMessageTypeEnum.ERROR_TALK, pTalkId);
	}
	
}
