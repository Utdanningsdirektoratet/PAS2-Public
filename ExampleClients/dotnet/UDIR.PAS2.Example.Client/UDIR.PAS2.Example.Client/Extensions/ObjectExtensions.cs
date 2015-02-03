using System;

namespace UDIR.PAS2.Example.Client.Extensions
{
    internal static class ObjectExtensions
    {
        public static void IfNotNull<T>(this T item, Action<T> action) where T : class
        {
            if (item != null) action(item);
        }
    }
}
