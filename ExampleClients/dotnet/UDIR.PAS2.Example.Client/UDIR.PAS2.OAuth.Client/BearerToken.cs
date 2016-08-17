using System;

namespace UDIR.PAS2.OAuth.Client
{
	internal class BearerToken
	{
		public BearerToken(DateTimeOffset expiryTime, string tokenValue)
		{
			ExpiryTime = expiryTime;
			Value = tokenValue;
		}

		public DateTimeOffset ExpiryTime { get; private set; }
		public string Value { get; private set; }
	}
}
